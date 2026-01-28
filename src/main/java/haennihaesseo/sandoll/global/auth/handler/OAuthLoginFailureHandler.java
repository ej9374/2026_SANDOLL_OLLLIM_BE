package haennihaesseo.sandoll.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import haennihaesseo.sandoll.global.response.ApiResponse;
import haennihaesseo.sandoll.global.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    log.error("로그인 실패: {}", exception.getMessage());
    exception.printStackTrace();

    // 응답 설정
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");

    // 에러 응답 생성
    ResponseEntity<ApiResponse<Void>> errorResponse = ApiResponse.fail(ErrorStatus.UNAUTHORIZED);

    // 응답에 에러 메시지 작성
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}

