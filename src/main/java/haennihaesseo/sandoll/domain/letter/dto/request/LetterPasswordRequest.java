package haennihaesseo.sandoll.domain.letter.dto.request;

import lombok.Getter;

@Getter
public class LetterPasswordRequest {
    private String secretLetterKey;
    private String password;
}
