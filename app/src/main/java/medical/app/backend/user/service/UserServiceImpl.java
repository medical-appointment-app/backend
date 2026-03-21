package medical.app.backend.user.service;

import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.common.model.person.User;
import medical.app.backend.user.dto.RegisterUserRequest;
import medical.app.backend.user.dto.UserResponse;
import medical.app.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(String externalId, String email, RegisterUserRequest request) {
        if (userRepository.existsByExternalId(externalId)) {
            throw new IllegalStateException("A profile already exists for this account");
        }

        User user = new User();
        user.setExternalId(externalId);
        user.setEmail(email);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setDateOfBirth(request.dateOfBirth());
        user.setAddress(request.address());
        user.setBloodType(request.bloodType());

        return UserResponse.from(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByExternalId(String externalId) {
        return userRepository.findByExternalId(externalId)
                .map(UserResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(String externalId) {
        return userRepository.existsByExternalId(externalId);
    }
}
