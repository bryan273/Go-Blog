package c6.goblogbackend.auth.service.authentication;

import c6.goblogbackend.auth.dto.authentication.AuthenticationResponse;
import c6.goblogbackend.auth.dto.authentication.LoginResponse;
import c6.goblogbackend.auth.dto.authentication.RegisterRequest;
import c6.goblogbackend.auth.dto.authentication.RegisterResponse;
import c6.goblogbackend.auth.exception.UserNotExistException;
import c6.goblogbackend.auth.model.User;

public interface AuthenticationService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(User user);

    AuthenticationResponse authenticate(String userEmail) throws UserNotExistException;
}
