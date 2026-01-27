package haennihaesseo.sandoll.global.auth.status;

import haennihaesseo.sandoll.global.base.BaseErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenErrorStatus implements BaseErrorStatus {
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_ACCESS_TOKEN", "유효하지 않은 액세스 토큰입니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "유효하지 않은 리프레쉬 토큰입니다."),
  INVALID_TMP_KEY(HttpStatus.UNAUTHORIZED, "INVALID_TMP_KEY", "유효하지 않은 임시 키입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
