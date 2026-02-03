package haennihaesseo.sandoll.domain.letter.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WritingLetterContentResponse {
  private String voiceUrl;
  private int duration;
  private Long fontId;
  private String fontName;
  private String fontUrl;
  private String content;
  private String title;
  private String sender;
  private String bgmUrl;
  private String templateUrl;
  private List<WritingWordInfo> words;

  @Builder
  public record WritingWordInfo(String word, Double startTime, Double endTime) {}
}
