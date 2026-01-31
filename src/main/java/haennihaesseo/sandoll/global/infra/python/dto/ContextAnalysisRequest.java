package haennihaesseo.sandoll.global.infra.python.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContextAnalysisRequest {
    private String content;
    private Integer count;
}
