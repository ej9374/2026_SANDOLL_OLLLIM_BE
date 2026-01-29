package haennihaesseo.sandoll.domain.font.exception;

import haennihaesseo.sandoll.global.base.BaseErrorStatus;
import haennihaesseo.sandoll.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class FontException extends GlobalException {

  public FontException(BaseErrorStatus errorStatus) {
    super(errorStatus);
  }
}
