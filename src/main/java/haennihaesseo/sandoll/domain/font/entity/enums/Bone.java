package haennihaesseo.sandoll.domain.font.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Bone {
    ROUND("포근한"), NATURAL("자연스러운"), ANGULAR("또렷한");

    private final String korean;
}
