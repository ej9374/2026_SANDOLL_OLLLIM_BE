package haennihaesseo.sandoll.domain.letter.service;

import haennihaesseo.sandoll.domain.letter.cache.CachedLetter;
import haennihaesseo.sandoll.domain.letter.cache.CachedLetterRepository;
import haennihaesseo.sandoll.domain.letter.exception.LetterException;
import haennihaesseo.sandoll.domain.letter.status.LetterErrorStatus;
import haennihaesseo.sandoll.global.infra.python.PythonAnalysisClient;
import haennihaesseo.sandoll.global.infra.python.dto.ContextAnalysisRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LetterContextService {
    private final LetterService letterService;
    private final CachedLetterRepository cachedLetterRepository;
    private final PythonAnalysisClient pythonAnalysisClient;

    public void recommendContextFont(String letterId){

    }

    private void contextAnalyze(String letterId){
        CachedLetter cachedLetter = cachedLetterRepository.findById(letterId)
                .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

        ContextAnalysisRequest request = new ContextAnalysisRequest(cachedLetter.getContent(), 1); // todo count=3 수정예정

        pythonAnalysisClient.requestContextAnalysis(request)
                .subscribe(event -> {
                    if ("analyze".equals(event.getStep())) {
                        // 분석 결과 처리
                    } else if ("done".equals(event.getStep())) {
                        // BGM 결과 처리
                    }
                });
    }

}
