package haennihaesseo.sandoll.domain.font.dto.response;

import haennihaesseo.sandoll.domain.font.entity.enums.FontType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecommendFontResponse {
  private FontType type;
  private List<WritingRecommendFont> fonts;

  @Builder
  public record WritingRecommendFont(Long fontId, String name, String fontUrl, List<String> keywords) {}

}
