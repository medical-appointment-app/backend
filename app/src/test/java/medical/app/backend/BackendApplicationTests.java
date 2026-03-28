package medical.app.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import medical.app.backend.session.SessionValidator;

@SpringBootTest
class BackendApplicationTests {

    // GatewaySessionValidator would call the external auth service at startup;
    // replace it with a mock so the context loads without a live gateway.
    @MockitoBean
    SessionValidator sessionValidator;

    @Test
    void contextLoads() {
    }
}
