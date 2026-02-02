package haennihaesseo.sandoll.domain.font.service;

import com.fasterxml.jackson.databind.JsonNode;
import haennihaesseo.sandoll.domain.font.dto.response.ContextFontResponse;
import haennihaesseo.sandoll.domain.font.entity.Font;
import haennihaesseo.sandoll.domain.font.entity.enums.*;
import haennihaesseo.sandoll.domain.font.repository.FontRepository;
import haennihaesseo.sandoll.domain.letter.cache.CachedLetter;
import haennihaesseo.sandoll.domain.letter.cache.CachedLetterRepository;
import haennihaesseo.sandoll.domain.letter.exception.LetterException;
import haennihaesseo.sandoll.domain.letter.status.LetterErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FontContextRecommendService {
    private final FontRepository fontRepository;
    private final CachedLetterRepository cachedLetterRepository;

    /**
     * gemini로 부터 추출된 내용을 통해 폰트 추천 및 저장
     * @param letterId
     * @param data
     */
    public void saveContextFontsInLetter(String letterId, JsonNode data){
        Bone bone = Bone.valueOf(data.get("bone").asText());
        Writer writer = Writer.valueOf(data.get("writer").asText());
        Situation situation = Situation.valueOf(data.get("situation").asText());
        Distance distance = Distance.valueOf(data.get("distance").asText());
        log.info("[문맥 키워드] Bone = {}, Writer = {},  Situation = {}, Distance = {}", bone, writer, situation, distance);

        List<Font> recommendContextFonts = recommendFonts(bone, writer, situation, distance);
        log.info("[문맥 분석 결과 추천 폰트] 추천된 폰트 이름 = {}", recommendContextFonts.stream().map(Font::getName).toList());
        cacheContextFonts(letterId, recommendContextFonts, bone, writer, situation, distance);
    }

    /**
     * 키워드 바탕으로 키워드 매칭 개수 높은 상위 3개 반환
     */
    private List<Font> recommendFonts(Bone bone, Writer writer, Situation situation, Distance distance) {
        return fontRepository.findByMatchScore(bone, writer, situation, distance, 1);
    }

    /**
     * 추천된 폰트 캐시에 저장
     */
    private void cacheContextFonts(String letterId, List<Font> contextFonts, Bone bone, Writer writer, Situation situation, Distance distance) {
        CachedLetter cachedLetter = cachedLetterRepository.findById(letterId)
                .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

        List<ContextFontResponse> contextFontResponses = contextFonts.stream()
                .map(font -> new ContextFontResponse(
                        font.getFontId(),
                        getMatchedKeywords(font, bone, writer, situation, distance)
                ))
                .toList();
        cachedLetter.setContextFonts(contextFontResponses);
        cachedLetterRepository.save(cachedLetter);
    }

    /**
     * 추천된 폰트와 매칭된 키워드 리스트 반환
     */
    private List<String> getMatchedKeywords(Font font, Bone bone, Writer writer, Situation situation, Distance distance) {
        List<String> matched = new ArrayList<>();
        if (font.getBoneKeyword() == bone) matched.add(bone.getKorean());
        if (font.getWriterKeyword() == writer) matched.add(writer.getKorean());
        if (font.getSituationKeyword() == situation) matched.add(situation.getKorean());
        if (font.getDistanceKeyword() == distance) matched.add(distance.getKorean());
        log.info("[폰트별 키워드] font = {}, matched = {}", font.getName() ,matched);
        return matched;
    }


}
