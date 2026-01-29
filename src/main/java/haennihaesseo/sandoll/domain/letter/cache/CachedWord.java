package haennihaesseo.sandoll.domain.letter.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedWord implements Serializable {

    private String word;
    private Double startTime;
    private Double endTime;
    private Double wordOrder;
}