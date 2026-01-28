package haennihaesseo.sandoll.global.auth.dto;

import lombok.Builder;
import lombok.Data;

public class TokenDto {

    @Data
    @Builder
    public static class TokenResponseDto {
        private Long userId;
        private String accessToken;
        private String refreshToken;
    }

    @Data
    @Builder
    public static class ReissueResponseDto {
        private String accessToken;
    }

}
