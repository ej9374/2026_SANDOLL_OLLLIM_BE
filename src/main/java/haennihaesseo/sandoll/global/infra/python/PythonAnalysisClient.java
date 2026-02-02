package haennihaesseo.sandoll.global.infra.python;

import haennihaesseo.sandoll.global.infra.python.dto.BgmStepEvent;
import haennihaesseo.sandoll.global.infra.python.dto.ContextAnalysisRequest;
import haennihaesseo.sandoll.global.infra.python.dto.PythonVoiceAnalysisRequest;
import haennihaesseo.sandoll.global.infra.python.dto.PythonVoiceAnalysisResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PythonAnalysisClient {

    private final WebClient webClient;

    public PythonAnalysisClient(@Value("${external.python-server-url}") String pythonServerUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(pythonServerUrl)
                .build();
    }

    public PythonVoiceAnalysisResponse requestVoiceAnalysis(PythonVoiceAnalysisRequest request) {
        log.info("[Python] 분석 요청 - voiceUrl: {}, 단어 수: {}", request.getVoiceUrl(), request.getWords().size());

        PythonVoiceAnalysisResponse response = webClient.post()
                .uri("/api/voice")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PythonVoiceAnalysisResponse.class)
                .block(Duration.ofSeconds(20)); // 최대 20초 대기, 추후 테스트 후 조정할 예정

        log.info("[Python] 분석 완료 - 추천 폰트: {}, 분석 결과: {}",
                response != null ? response.getRecommendedFonts() : "null",
                response != null ? response.getAnalysisResult() : "null");

        return response;
    }

    public Flux<BgmStepEvent> requestContextAnalysis(ContextAnalysisRequest request){
        log.info("[Python] 분석 요청 - content: {}", request.getContent());

        return webClient.post()
                .uri("/api/bgm")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                // todo 4xx, 5xx 에러시 에러코드 추가 예정
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<BgmStepEvent>>() {})
                .mapNotNull(ServerSentEvent::data)
                .timeout(Duration.ofMinutes(3)) // todo 3분 타임어택 걸어두었으나 추후 수정 예정
                .doOnNext(event -> {
                    log.info("[Python] step: {}", event.getStep());
                })
                .doOnError(error -> {
                    log.error("[Python] 에러 발생: {}", error.getMessage());
                });
    }

}