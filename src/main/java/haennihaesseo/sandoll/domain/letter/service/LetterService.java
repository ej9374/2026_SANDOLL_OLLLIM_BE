package haennihaesseo.sandoll.domain.letter.service;

import haennihaesseo.sandoll.domain.letter.converter.LetterBoxConverter;
import haennihaesseo.sandoll.domain.letter.dto.request.LetterType;
import haennihaesseo.sandoll.domain.letter.dto.request.OrderStatus;
import haennihaesseo.sandoll.domain.letter.dto.response.*;
import haennihaesseo.sandoll.domain.letter.entity.Letter;
import haennihaesseo.sandoll.domain.letter.entity.LetterStatus;
import haennihaesseo.sandoll.domain.letter.entity.ReceiverLetterId;
import haennihaesseo.sandoll.domain.letter.entity.Word;
import haennihaesseo.sandoll.domain.letter.repository.WordRepository;
import haennihaesseo.sandoll.domain.letter.status.LetterErrorStatus;
import haennihaesseo.sandoll.domain.letter.exception.LetterException;
import haennihaesseo.sandoll.domain.letter.repository.LetterRepository;
import haennihaesseo.sandoll.domain.letter.repository.ReceiverLetterRepository;
import haennihaesseo.sandoll.domain.user.repository.UserRepository;
import haennihaesseo.sandoll.global.exception.GlobalException;
import haennihaesseo.sandoll.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LetterService {

    private final LetterRepository letterRepository;
    private final ReceiverLetterRepository receiverLetterRepository;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final LetterBoxConverter letterBoxConverter;

    public List<ReceiveLetterResponse> getReceivedLettersByUser(Long userId, OrderStatus status) {

        userRepository.findById(userId).orElseThrow(() -> new GlobalException(ErrorStatus.USER_NOT_FOUND));

        List<Long> letterIds = (status.equals(OrderStatus.EARLIEST))
                ? receiverLetterRepository.findIdLetterIdByIdReceiverIdOrderByCreatedAtDesc(userId)
                : receiverLetterRepository.findIdLetterIdByIdReceiverIdOrderByCreatedAtAsc(userId);

        List<ReceiveLetterResponse> results = new ArrayList<>();

        for (Long letterId : letterIds) {
            Letter letter = letterRepository.findById(letterId)
                    .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

            results.add(
                    ReceiveLetterResponse.builder()
                            .letterId(letterId)
                            .sender(letter.getSenderName())
                            .createdAt(letter.getCreatedAt().toLocalDate())
                            .build()
            );
        }
        return results;
    }

    public LetterDetailResponse getLetterDetailsByLetter(Long letterId) {

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

        log.info("최종 응답 생성");

        return letterBoxConverter.toLetterDetailResponse(letter, letter.getBgm(),
                        letter.getTemplate(), letter.getDefaultFont(),
                        letter.getVoice(), words);
    }

    public void hideLetter(Long userId, LetterType letterType, List<Long> letterIds) {

        userRepository.findById(userId).orElseThrow(() -> new GlobalException(ErrorStatus.USER_NOT_FOUND));

        if (letterType.equals(LetterType.RECEIVE)){
            for (Long letterId : letterIds) {
                ReceiverLetterId id = new ReceiverLetterId(userId, letterId);
                if (!receiverLetterRepository.existsById(id))
                    throw new LetterException(LetterErrorStatus.NOT_LETTER_OWNER);
                receiverLetterRepository.deleteById(id);
            }
        } else {
            for (Long letterId : letterIds) {
                Letter letter = letterRepository.findByLetterIdAndSenderUserId(letterId, userId);
                if (letter == null)
                    throw new LetterException(LetterErrorStatus.NOT_LETTER_OWNER);
                letter.setLetterStatus(LetterStatus.INVISIBLE);
                letterRepository.save(letter);
            }
        }
    }

    public List<SendLetterResponse> getSentLettersByUser(Long userId, OrderStatus status) {

        userRepository.findById(userId).orElseThrow(() -> new GlobalException(ErrorStatus.USER_NOT_FOUND));

        List<Long> letterIds = (status.equals(OrderStatus.EARLIEST))
                ? letterRepository.findIdLetterIdBySenderUserIdOrderByCreatedAtDesc(userId, LetterStatus.VISIBLE)
                : letterRepository.findIdLetterIdBySenderUserIdOrderByCreatedAtAsc(userId, LetterStatus.VISIBLE);

        List<SendLetterResponse> results = new ArrayList<>();

        for (Long letterId : letterIds) {
            Letter letter = letterRepository.findById(letterId)
                    .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

            results.add(
                    SendLetterResponse.builder()
                            .letterId(letterId)
                            .title(letter.getTitle())
                            .createdAt(letter.getCreatedAt().toLocalDate())
                            .build()
            );
        }
        return results;
    }
}
