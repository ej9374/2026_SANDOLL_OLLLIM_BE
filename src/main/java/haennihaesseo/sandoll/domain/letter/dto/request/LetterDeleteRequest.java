package haennihaesseo.sandoll.domain.letter.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class LetterDeleteRequest {

    @NotBlank
    private LetterType type;

    @NotBlank(message = "삭제하려는 편지를 선택해주세요.")
    private List<Long> letterIds;
}
