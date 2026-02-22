package medical.app.backend.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import medical.app.backend.common.context.RequestContext;
import medical.app.backend.common.exception.UnauthorizedException;
import medical.app.backend.session.dto.SessionInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionValidationInterceptor implements HandlerInterceptor {

    static final String SESSION_TOKEN_HEADER = "X-Session-Token";

    private final SessionValidator sessionValidator;

    public SessionValidationInterceptor(SessionValidator sessionValidator) {
        this.sessionValidator = sessionValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(SESSION_TOKEN_HEADER);
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("Missing session token");
        }

        SessionInfo sessionInfo = sessionValidator.validate(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired session"));

        RequestContext.setSessionInfo(sessionInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        RequestContext.clear();
    }
}
