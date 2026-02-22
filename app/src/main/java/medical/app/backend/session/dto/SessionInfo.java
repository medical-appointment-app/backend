package medical.app.backend.session.dto;

import java.util.List;

/**
 * Represents the validated identity returned by the external auth gateway.
 */
public record SessionInfo(
        String userId,
        String email,
        List<String> roles) {
}
