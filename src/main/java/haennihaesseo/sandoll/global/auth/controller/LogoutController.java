package haennihaesseo.sandoll.global.auth.controller;

import haennihaesseo.sandoll.global.auth.principal.UserPrincipal;
import haennihaesseo.sandoll.global.auth.service.TokenService;
import haennihaesseo.sandoll.global.response.ApiResponse;
import haennihaesseo.sandoll.global.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Logout", description = "로그아웃 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class LogoutController {

  private final TokenService tokenService;

  @Operation(
      summary = "[7.2] 로그아웃"
  )
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<String>> logout(
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    tokenService.logout(userPrincipal.getUser().getUserId());
    return ApiResponse.success(SuccessStatus.CREATED);
  }

}
