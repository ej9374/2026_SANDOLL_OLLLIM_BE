package haennihaesseo.sandoll.domain.font.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FontSettingRequest {
  @NotNull
  private Long fontId;

}
