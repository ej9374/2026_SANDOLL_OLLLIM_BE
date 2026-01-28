package haennihaesseo.sandoll.domain.letter.dto.request;

import lombok.Getter;

@Getter
public class LetterLinkViewRequest {
    private String secretLetterId;
    private String password;
}
