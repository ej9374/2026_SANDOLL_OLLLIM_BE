package haennihaesseo.sandoll.domain.font.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Writer {
    JUNIOR("발랄한"), YOUTH("젊은"), SENIOR("성숙한");

    private final String korean;
}
