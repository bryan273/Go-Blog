package c6.goblogbackend.auth.service.user;

import org.springframework.stereotype.Service;

import c6.goblogbackend.auth.dto.user.EditProfileRequest;
import c6.goblogbackend.auth.dto.user.EditProfileResponse;
import c6.goblogbackend.auth.dto.user.UserProfileResponse;
import c6.goblogbackend.auth.model.User;
import c6.goblogbackend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;

    @Override
    public UserProfileResponse getUserProfile(User user) {

        return UserProfileResponse.builder()
                                    .fullname(user.getFullname())
                                    .username(user.getUsername())
                                    .email(user.getEmail())
                                    .birthdate(user.getBirthdate())
                                    .gender(user.getGender().name())
                                    .build();
    }

    @Override
    public synchronized EditProfileResponse editUserProfile(EditProfileRequest requestBody, User user){
        user.setUsername(requestBody.getUsername());
        user.setFullname(requestBody.getFullname());
        userRepository.saveAndFlush(user);
        
        return EditProfileResponse.builder()
                                .fullname(requestBody.getFullname())
                                .username(requestBody.getUsername())
                                .email(user.getEmail())
                                .build();
    }
}
