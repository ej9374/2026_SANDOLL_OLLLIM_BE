package haennihaesseo.sandoll.global.infra.stt;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;
import haennihaesseo.sandoll.global.exception.GlobalException;
import haennihaesseo.sandoll.global.status.ErrorStatus;
import haennihaesseo.sandoll.global.util.ResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class GoogleSttClient {

    private static final String LANGUAGE_CODE = "ko-KR";

    private final SpeechSettings speechSettings;

    public GoogleSttClient() {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    ResourceLoader.getResourceAsStream("google-stt-key.json"));
            this.speechSettings = SpeechSettings.newBuilder()
                    .setCredentialsProvider(() -> credentials)
                    .build();
        } catch (IOException e) {
            throw new GlobalException(ErrorStatus.STT_SERVICE_ERROR);
        }
    }

    public SttResult transcribe(MultipartFile audioFile, int duration) {
        try {
            byte[] audioBytes = audioFile.getBytes();
            RecognitionConfig.AudioEncoding encoding = AudioEncoding.WEBM_OPUS;

            log.info("[STT 요청] duration={}, 파일크기={}bytes, contentType={}, 파일명={}",
                    duration, audioBytes.length, audioFile.getContentType(), audioFile.getOriginalFilename());

            return transcribeAudio(audioBytes, encoding, duration);
        } catch (IOException e) {
            log.error("오디오 파일 읽기 실패", e);
            throw new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private SttResult transcribeAudio(byte[] audioBytes, RecognitionConfig.AudioEncoding encoding, int duration) {
        try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {
            RecognitionConfig config = buildConfig(encoding);

            log.info("[STT Config] encoding={}, language={}, model={}",
                    config.getEncoding(), config.getLanguageCode(), config.getModel());

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioBytes))
                    .build();

            log.info("[STT Audio] content 크기={}bytes", audio.getContent().size());

            List<SpeechRecognitionResult> results;

            if (duration <= 15) {
                log.info("[STT] 동기 처리 시작 (duration={})", duration);
                RecognizeResponse response = speechClient.recognize(config, audio);
                log.info("[STT] 동기 처리 완료, 결과 개수={}", response.getResultsCount());
                results = response.getResultsList();
            } else {
                log.info("[STT] 비동기 처리 시작 (duration={})", duration);
                var operationFuture = speechClient.longRunningRecognizeAsync(config, audio);
                log.info("[STT] 비동기 요청 전송됨, 응답 대기 중...");
                LongRunningRecognizeResponse response = operationFuture.get();
                log.info("[STT] 비동기 처리 완료, 결과 개수={}", response.getResultsCount());
                results = response.getResultsList();
            }

            return parseResults(results);
        } catch (IOException e) {
            log.error("[STT 에러] IOException: {}", e.getMessage(), e);
            throw new GlobalException(ErrorStatus.STT_SERVICE_ERROR);
        } catch (InvalidArgumentException e) {
            log.error("[STT 에러] InvalidArgumentException: {}", e.getMessage(), e);
            throw new GlobalException(ErrorStatus.STT_SERVICE_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[STT 에러] InterruptedException: {}", e.getMessage(), e);
            throw new GlobalException(ErrorStatus.STT_SERVICE_ERROR);
        } catch (ExecutionException e) {
            log.error("[STT 에러] ExecutionException - 원인: {}", e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
            throw new GlobalException(ErrorStatus.STT_SERVICE_ERROR);
        }
    }

    private RecognitionConfig buildConfig(RecognitionConfig.AudioEncoding encoding) {
        RecognitionConfig.Builder builder = RecognitionConfig.newBuilder()
                .setEncoding(encoding)
                .setLanguageCode(LANGUAGE_CODE)
                .setEnableWordTimeOffsets(true)
                .setEnableAutomaticPunctuation(true)
//                .setSampleRateHertz(48000) // webm 의 일반적인 샘플레이트, 현재 프론트에서 webm 만 보내므로 고정
                .setModel("default");
        // setSampleRateHertz 생략 시 Google이 자동 감지

        return builder.build();
    }

    private SttResult parseResults(List<SpeechRecognitionResult> resultsList) {
        if (resultsList.isEmpty()) {
            return SttResult.builder()
                    .fullText("")
                    .totalDuration(0)
                    .words(Collections.emptyList())
                    .build();
        }

        List<SttWord> words = new ArrayList<>();
        StringBuilder fullText = new StringBuilder();
        int totalDuration = 0;
        double wordOrder = 0;

        for (SpeechRecognitionResult result : resultsList) {
            if (result.getAlternativesCount() == 0) continue;

            SpeechRecognitionAlternative alternative = result.getAlternatives(0);
            fullText.append(alternative.getTranscript()).append(" ");
            totalDuration = (int) toSeconds(result.getResultEndTime());

            for (WordInfo wordInfo : alternative.getWordsList()) {
                double startTime = toSeconds(wordInfo.getStartTime());
                double endTime = toSeconds(wordInfo.getEndTime());

                words.add(SttWord.builder()
                        .word(wordInfo.getWord())
                        .startTime(startTime)
                        .endTime(endTime)
                        .order(wordOrder++)
                        .build());
            }
        }

        return SttResult.builder()
                .fullText(fullText.toString().trim())
                .totalDuration(totalDuration)
                .words(words)
                .build();
    }

    private double toSeconds(com.google.protobuf.Duration duration) {
        return duration.getSeconds() + duration.getNanos() / 1_000_000_000.0;
    }
}