package haennihaesseo.sandoll.global.auth.controller;

import haennihaesseo.sandoll.global.auth.dto.TokenDto;
import haennihaesseo.sandoll.global.auth.service.TokenService;
import haennihaesseo.sandoll.global.response.ApiResponse;
import haennihaesseo.sandoll.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

  private final TokenService tokenService;

  @GetMapping
  public ResponseEntity<ApiResponse<TokenDto.TokenResponseDto>> issueToken(
      @RequestHeader("tmpKey") String tmpKey
  ) {
    TokenDto.TokenResponseDto issueTokenResponse = tokenService.issueTokens(tmpKey);

    return ApiResponse.success(SuccessStatus.OK, issueTokenResponse);
  }

  @GetMapping("/reissue")
  public ResponseEntity<ApiResponse<TokenDto.ReissueResponseDto>> reissueAccessToken(
      @RequestHeader("refreshToken") String refreshToken
  ) {
    TokenDto.ReissueResponseDto accessToken = tokenService.reissueAccessToken(refreshToken);

    return ApiResponse.success(SuccessStatus.OK, accessToken);
  }

}
