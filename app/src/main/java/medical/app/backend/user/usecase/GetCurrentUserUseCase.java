package medical.app.backend.user.usecase;

import medical.app.backend.common.context.RequestContext;
import medical.app.backend.user.dto.UserResponse;
import medical.app.backend.user.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class GetCurrentUserUseCase {

    private final UserService userService;

    public GetCurrentUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public UserResponse execute() {
        String externalId = RequestContext.getSessionInfo().userId();
        return userService.getByExternalId(externalId);
    }
}
