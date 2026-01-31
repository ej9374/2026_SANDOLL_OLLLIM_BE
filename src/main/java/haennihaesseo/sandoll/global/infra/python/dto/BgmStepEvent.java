package haennihaesseo.sandoll.global.infra.python.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class BgmStepEvent {
    private String step;
    private JsonNode data;  // 또는 Map<String, Object>
}