package haennihaesseo.sandoll.domain.letter.service;

import haennihaesseo.sandoll.domain.letter.converter.LetterBoxConverter;
import haennihaesseo.sandoll.domain.letter.dto.response.LetterDetailResponse;
import haennihaesseo.sandoll.domain.letter.entity.Letter;
import haennihaesseo.sandoll.domain.letter.entity.Word;
import haennihaesseo.sandoll.domain.letter.exception.LetterException;
import haennihaesseo.sandoll.domain.letter.repository.LetterRepository;
import haennihaesseo.sandoll.domain.letter.repository.WordRepository;
import haennihaesseo.sandoll.domain.letter.status.LetterErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LetterDetailService {

    private final LetterRepository letterRepository;
    private final WordRepository wordRepository;
    private final LetterBoxConverter letterBoxConverter;

    public LetterDetailResponse getLetterDetails(Long letterId) {
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

        log.info("단어 조회 시작");
        List<Word> words = wordRepository.findByLetterLetterIdOrderByWordOrderAsc(letterId);
        List<LetterDetailResponse.WordInfo> wordInfos = new ArrayList<>();

        log.info("word를 wordInfo 리스트로 변환시작");
        for (Word word : words) {
            wordInfos.add(
                    LetterDetailResponse.WordInfo.builder()
                            .wordId(word.getWordId())
                            .word(word.getWord())
                            .startTime(word.getStartTime())
                            .endTime(word.getEndTime())
                            .build()
            );
        }
        return letterBoxConverter.toLetterDetailResponse(letter, letter.getBgm(),
                letter.getTemplate(), letter.getDefaultFont(),
                letter.getVoice(), words);
    }
}
