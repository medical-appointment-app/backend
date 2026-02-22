package medical.app.backend.session;

import medical.app.backend.session.dto.SessionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

/**
 * Validates sessions by forwarding the token to the external auth gateway.
 * The gateway is expected to return a SessionInfo body on 200 OK,
 * or a non-2xx status for invalid/expired tokens.
 */
@Component
public class GatewaySessionValidator implements SessionValidator {

    private final RestClient restClient;

    @Value("${app.auth.gateway-validate-url}")
    private String gatewayValidateUrl;

    public GatewaySessionValidator(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @Override
    public Optional<SessionInfo> validate(String token) {
        try {
            SessionInfo sessionInfo = restClient.get()
                    .uri(gatewayValidateUrl)
                    .header("X-Session-Token", token)
                    .retrieve()
                    .body(SessionInfo.class);
            return Optional.ofNullable(sessionInfo);
        } catch (RestClientException ex) {
            return Optional.empty();
        }
    }
}
