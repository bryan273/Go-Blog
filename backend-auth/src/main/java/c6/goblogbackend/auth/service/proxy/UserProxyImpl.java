package c6.goblogbackend.auth.service.proxy;


import org.springframework.stereotype.Service;
import c6.goblogbackend.auth.dto.user.EditProfileRequest;
import c6.goblogbackend.auth.dto.user.EditProfileResponse;
import c6.goblogbackend.auth.dto.user.UserProfileResponse;
import c6.goblogbackend.auth.exception.ExpiredJWTException;
import c6.goblogbackend.auth.exception.InvalidJWTException;
import c6.goblogbackend.auth.exception.UserNotExistException;
import c6.goblogbackend.auth.model.User;
import c6.goblogbackend.auth.repository.UserRepository;
import c6.goblogbackend.auth.service.JWTService;
import c6.goblogbackend.auth.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProxyImpl implements UserProxy{

    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;
    private final JWTService jwtService;

    @Override
    public UserProfileResponse handleRead(String jwt){
        var user = validateJWTAndGetUser(jwt);
        return userServiceImpl.getUserProfile(user);
    }


    @Override
    public EditProfileResponse handleUpdate(String jwt, EditProfileRequest request){
        var user = validateJWTAndGetUser(jwt);
        return userServiceImpl.editUserProfile(request, user);
    }


    private void verifyJWT(String jwt){
        if (!jwtService.verify(jwt)) throw new InvalidJWTException();
        if (jwtService.isTokenExpired(jwt)) throw new ExpiredJWTException();
    }

    private User validateJWTAndGetUser(String jwt) {
        verifyJWT(jwt);
        String userEmail = jwtService.extractSubject(jwt);
        return userRepository.findByEmail(userEmail).orElseThrow(UserNotExistException::new);
    }
}