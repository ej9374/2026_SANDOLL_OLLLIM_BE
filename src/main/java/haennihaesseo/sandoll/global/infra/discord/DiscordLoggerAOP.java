package haennihaesseo.sandoll.global.infra.discord;

import haennihaesseo.sandoll.global.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DiscordLoggerAOP {

  private final DiscordAlarmSender discordAlarmSender;

  @Pointcut("execution(* haennihaesseo.sandoll.global.exception.GlobalExceptionHandler.handleGeneralException(..))")
  public void generalExceptionErrorLoggerExecute() {}

  @Pointcut("execution(* haennihaesseo.sandoll.global.exception.GlobalExceptionHandler.handleException(..))")
  public void serverExceptionErrorLoggerExecute() {}

  @Before("generalExceptionErrorLoggerExecute()")
  public void generalExceptionLogging(JoinPoint joinpoint) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    GlobalException exception = (GlobalException) joinpoint.getArgs()[0];

    if (exception.getErrorStatus().getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR)
      discordAlarmSender.sendDiscordAlarm(exception, request);
  }

  @Before("serverExceptionErrorLoggerExecute()")
  public void serverExceptionLogging(JoinPoint joinpoint) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    Exception exception = (Exception)joinpoint.getArgs()[0];

    discordAlarmSender.sendDiscordAlarm(exception, request);
  }
}

