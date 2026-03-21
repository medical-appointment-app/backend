package medical.app.backend.user.controller;

import jakarta.validation.Valid;
import medical.app.backend.common.model.ApiResponse;
import medical.app.backend.common.web.ResponseBuilder;
import medical.app.backend.user.dto.RegisterUserRequest;
import medical.app.backend.user.dto.UserResponse;
import medical.app.backend.user.usecase.GetCurrentUserUseCase;
import medical.app.backend.user.usecase.RegisterUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase,
                          GetCurrentUserUseCase getCurrentUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterUserRequest request) {
        return ResponseBuilder.created(registerUserUseCase.execute(request));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        return ResponseBuilder.ok(getCurrentUserUseCase.execute());
    }
}
