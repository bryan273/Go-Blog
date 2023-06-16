package c6.goblogbackend.auth.service.proxy;

import c6.goblogbackend.auth.dto.user.EditProfileRequest;
import c6.goblogbackend.auth.dto.user.EditProfileResponse;
import c6.goblogbackend.auth.dto.user.UserProfileResponse;

public interface UserProxy {

    UserProfileResponse handleRead(String jwt);

    EditProfileResponse handleUpdate(String jwt, EditProfileRequest request);
}
