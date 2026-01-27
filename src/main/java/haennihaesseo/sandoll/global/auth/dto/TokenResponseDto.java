package haennihaesseo.sandoll.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
