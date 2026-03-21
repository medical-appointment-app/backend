package medical.app.backend.user.usecase;

import medical.app.backend.common.context.RequestContext;
import medical.app.backend.user.dto.RegisterUserRequest;
import medical.app.backend.user.dto.UserResponse;
import medical.app.backend.user.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserUseCase {

    private final UserService userService;

    public RegisterUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public UserResponse execute(RegisterUserRequest request) {
        String externalId = RequestContext.getSessionInfo().userId();
        String email      = RequestContext.getSessionInfo().email();
        return userService.register(externalId, email, request);
    }
}
