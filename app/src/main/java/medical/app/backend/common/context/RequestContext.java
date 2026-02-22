package medical.app.backend.common.context;

import medical.app.backend.session.dto.SessionInfo;

/**
 * Thread-local holder for the validated session of the current request.
 * Populated by SessionValidationInterceptor and cleared after the request completes.
 */
public final class RequestContext {

    private static final ThreadLocal<SessionInfo> SESSION_HOLDER = new ThreadLocal<>();

    private RequestContext() {}

    public static void setSessionInfo(SessionInfo sessionInfo) {
        SESSION_HOLDER.set(sessionInfo);
    }

    public static SessionInfo getSessionInfo() {
        return SESSION_HOLDER.get();
    }

    public static void clear() {
        SESSION_HOLDER.remove();
    }
}
