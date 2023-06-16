package c6.goblogbackend.auth.service.user;

import c6.goblogbackend.auth.dto.user.EditProfileRequest;
import c6.goblogbackend.auth.dto.user.EditProfileResponse;
import c6.goblogbackend.auth.dto.user.UserProfileResponse;
import c6.goblogbackend.auth.model.User;

public interface UserService {

    UserProfileResponse getUserProfile(User user);

    EditProfileResponse editUserProfile(EditProfileRequest requestBody, User user);
}
