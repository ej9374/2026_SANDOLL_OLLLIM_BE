package haennihaesseo.sandoll.global.auth.handler;

import haennihaesseo.sandoll.domain.user.entity.Provider;
import haennihaesseo.sandoll.domain.user.entity.User;
import haennihaesseo.sandoll.domain.user.repository.UserRepository;
import haennihaesseo.sandoll.global.auth.dto.KakaoUserInfo;
import haennihaesseo.sandoll.global.auth.dto.OAuth2UserInfo;
import haennihaesseo.sandoll.global.auth.util.JwtUtil;
import haennihaesseo.sandoll.global.exception.GlobalException;
import haennihaesseo.sandoll.global.infra.RedisClient;
import haennihaesseo.sandoll.global.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Value("${jwt.redirect-url}")
  private String REDIRECT_URL;

  @Value("${jwt.tmp-expiration-ms}")
  private long TMP_KEY_EXPIRATION_MS;

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final RedisClient redisClient;


  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    log.info("OAuth2 로그인 성공 핸들러 호출됨");
    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
    OAuth2UserInfo oAuth2UserInfo = extractOAuth2UserInfo(token, token.getAuthorizedClientRegistrationId());

    String providerId = oAuth2UserInfo.getProviderId();
    String provider = oAuth2UserInfo.getProvider();
    log.info("providerId: {}", providerId);
    log.info("provider: {}", provider);

    Optional<User> optionalUser = userRepository.findByProviderId(providerId);

    if (optionalUser.isPresent()) {
      handleExistingUser(request, response, optionalUser.get());
    } else {
      handleNewUser(request, response, providerId, provider);
    }
  }

  private OAuth2UserInfo extractOAuth2UserInfo(OAuth2AuthenticationToken token, String provider) {
    // 추후 확장시 추가 예정
    switch (provider) {
      case "kakao":
        return new KakaoUserInfo(token.getPrincipal().getAttributes());
      default:
        throw new GlobalException(ErrorStatus.BAD_REQUEST);
    }
  }

  // 기존 유저 처리
  private void handleExistingUser(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
    log.info("기존 유저입니다. 임시 키를 발급합니다.");

    // 임시 키 생성
    String tmpKey = jwtUtil.generateTmpKey(user.getUserId());
    String redirectURI = String.format(REDIRECT_URL, tmpKey);

    getRedirectStrategy().sendRedirect(request, response, redirectURI);
  }

  // 신규 유저 처리
  private void handleNewUser(HttpServletRequest request, HttpServletResponse response, String providerId, String provider) throws IOException {
    log.info("신규 유저입니다. 회원가입 진행 후 임시 키를 발급합니다.");

    // 유저 생성
    User newUser = User.builder()
        .providerId(providerId)
        .provider(Provider.KAKAO) // 추후 확장 시 변경 예정
        .build();
    User user = userRepository.save(newUser);

    // 임시 키 생성
    String tmpKey = jwtUtil.generateTmpKey(user.getUserId());
    redisClient.setData("TMP_KEY", tmpKey, user.getUserId().toString(), TMP_KEY_EXPIRATION_MS);
    String redirectURI = String.format(REDIRECT_URL, tmpKey);
    getRedirectStrategy().sendRedirect(request, response, redirectURI);
  }

}
