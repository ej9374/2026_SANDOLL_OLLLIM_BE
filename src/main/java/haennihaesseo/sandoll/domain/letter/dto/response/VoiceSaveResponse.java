package haennihaesseo.sandoll.domain.letter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.w3c.dom.Text;

@Getter
@AllArgsConstructor
@Builder
public class VoiceSaveResponse {
  private String letterId;
  private String letterKey;
  private String voiceUrl;
  private int duration;
  private String content;
}
