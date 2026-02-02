package haennihaesseo.sandoll.domain.font.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Distance {
    CLOSE("친밀한"), NEUTRAL("덤덤한"), FAR("정중한");

    private final String korean;
}
