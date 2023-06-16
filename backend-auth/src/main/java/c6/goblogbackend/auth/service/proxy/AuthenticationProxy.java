package c6.goblogbackend.auth.service.proxy;

import c6.goblogbackend.auth.dto.authentication.AuthenticationResponse;
import c6.goblogbackend.auth.dto.authentication.LoginRequest;
import c6.goblogbackend.auth.dto.authentication.LoginResponse;
import c6.goblogbackend.auth.dto.authentication.RegisterRequest;
import c6.goblogbackend.auth.dto.authentication.RegisterResponse;
import c6.goblogbackend.auth.exception.ExpiredJWTException;
import c6.goblogbackend.auth.exception.InvalidJWTException;

public interface AuthenticationProxy {

    RegisterResponse handleRegister(RegisterRequest request);

    LoginResponse handleLogin(LoginRequest request);

    AuthenticationResponse handleAuthenticate(String apiKey, String jwt) throws InvalidJWTException, ExpiredJWTException;
}
