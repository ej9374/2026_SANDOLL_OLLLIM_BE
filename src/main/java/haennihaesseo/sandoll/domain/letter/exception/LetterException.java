package haennihaesseo.sandoll.domain.letter.exception;

import haennihaesseo.sandoll.global.base.BaseErrorStatus;
import haennihaesseo.sandoll.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class LetterException extends GlobalException {

   public LetterException(BaseErrorStatus errorStatus) {
      super(errorStatus);
   }
}
