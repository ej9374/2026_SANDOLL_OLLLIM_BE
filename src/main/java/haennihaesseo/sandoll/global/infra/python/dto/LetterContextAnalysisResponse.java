package haennihaesseo.sandoll.global.infra.python.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LetterContextAnalysisResponse {
    private List<LetterContextAnalysisResponse.RecommendFont> fonts;

    @Builder
    public record RecommendFont(Long fontId, String name, String fontUrl) {}
}
