package medical.app.backend.user.service;

import medical.app.backend.user.dto.RegisterUserRequest;
import medical.app.backend.user.dto.UserResponse;

public interface UserService {

    UserResponse register(String externalId, String email, RegisterUserRequest request);

    UserResponse getByExternalId(String externalId);

    boolean exists(String externalId);
}
