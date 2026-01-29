package haennihaesseo.sandoll.domain.font.status;

import haennihaesseo.sandoll.global.base.BaseErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FontErrorStatus implements BaseErrorStatus {

  FONT_NOT_FOUND(HttpStatus.NOT_FOUND, "FONT_NOT_FOUND", "해당 폰트가 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
