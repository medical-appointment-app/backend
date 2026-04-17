package medical.app.backend.session;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import medical.app.backend.common.context.RequestContext;
import medical.app.backend.common.exception.UnauthorizedException;
import medical.app.backend.session.dto.SessionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class SessionValidationInterceptor implements HandlerInterceptor {

    static final String SESSION_TOKEN_HEADER = "X-Session-Token";

    private static final Logger log = LoggerFactory.getLogger(SessionValidationInterceptor.class);

    private final SessionValidator sessionValidator;

    /**
     * Master switch for session validation. Defaults to {@code true}.
     * <p>
     * Set {@code app.auth.enabled=false} in {@code application.properties} to pause
     * session checks during development — the interceptor will install a stub
     * {@link SessionInfo} on every request so downstream code
     * (e.g. {@code RequestContext.getSessionInfo().userId()}) keeps working.
     */
    @Value("${app.auth.enabled:true}")
    private boolean authEnabled;

    /** The fake user ID used when auth is disabled. Should match a seeded user's external_id. */
    @Value("${app.auth.dev-user-id:ext-user-001}")
    private String devUserId;

    @Value("${app.auth.dev-user-email:dev@local}")
    private String devUserEmail;

    public SessionValidationInterceptor(SessionValidator sessionValidator) {
        this.sessionValidator = sessionValidator;
    }

    @PostConstruct
    @SuppressWarnings("unused") // invoked by the Spring lifecycle via @PostConstruct
    void warnIfDisabled() {
        if (!authEnabled) {
            log.warn("╔══════════════════════════════════════════════════════════════════╗");
            log.warn("║  SESSION AUTH IS DISABLED (app.auth.enabled=false)              ║");
            log.warn("║  Every request is treated as userId='{}'               ║", devUserId);
            log.warn("║  DO NOT SHIP THIS CONFIG TO PRODUCTION.                         ║");
            log.warn("╚══════════════════════════════════════════════════════════════════╝");
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!authEnabled) {
            RequestContext.setSessionInfo(new SessionInfo(devUserId, devUserEmail, List.of("PATIENT")));
            return true;
        }

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
