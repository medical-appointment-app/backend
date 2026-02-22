package medical.app.backend.session;

import medical.app.backend.session.dto.SessionInfo;

import java.util.Optional;

public interface SessionValidator {

    /**
     * Validates the given session token against the external auth gateway.
     *
     * @param token the raw value of the X-Session-Token header
     * @return the caller's identity if the token is valid, or empty if invalid/expired
     */
    Optional<SessionInfo> validate(String token);
}
