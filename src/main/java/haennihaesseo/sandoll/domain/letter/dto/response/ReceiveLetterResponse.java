package haennihaesseo.sandoll.domain.letter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class ReceiveLetterResponse {
    private Long letterId;
    private String sender;
    private LocalDate createdAt;
}
