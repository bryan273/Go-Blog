package c6.goblogbackend.auth.service.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import c6.goblogbackend.auth.dto.authentication.AuthenticationResponse;
import c6.goblogbackend.auth.dto.authentication.LoginRequest;
import c6.goblogbackend.auth.dto.authentication.LoginResponse;
import c6.goblogbackend.auth.dto.authentication.RegisterRequest;
import c6.goblogbackend.auth.dto.authentication.RegisterResponse;
import c6.goblogbackend.auth.exception.EmailAlreadyExistException;
import c6.goblogbackend.auth.exception.ExpiredJWTException;
import c6.goblogbackend.auth.exception.InvalidApiKeyException;
import c6.goblogbackend.auth.exception.InvalidJWTException;
import c6.goblogbackend.auth.exception.UsernameAlreadyExistException;
import c6.goblogbackend.auth.repository.UserRepository;
import c6.goblogbackend.auth.service.JWTService;
import c6.goblogbackend.auth.service.authentication.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthenticationProxyImpl implements AuthenticationProxy{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationServiceImpl;
    @Value("${api-key}")
    private String apiKey;

    private final JWTService jwtService;

    @Override
    public RegisterResponse handleRegister(RegisterRequest request){
        checkEmailExistence(request.getEmail());
        checkUsernameExistence(request.getUsername());

        return authenticationServiceImpl.register(request);
    }

    @Override
    public LoginResponse handleLogin(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return authenticationServiceImpl.login(user);
    }

    @Override
    public AuthenticationResponse handleAuthenticate(String apiKey, String jwt) throws InvalidJWTException, ExpiredJWTException{

        checkApiKey(apiKey);
        jwtService.verify(jwt);  
        String userEmail = jwtService.extractSubject(jwt);

        return authenticationServiceImpl.authenticate(userEmail);
    }

    public void checkApiKey(String key) throws InvalidApiKeyException{
        if(key == null) throw new InvalidApiKeyException();
        var requestApiKey = key.substring(7);
        if(!requestApiKey.equals(this.apiKey)) throw new InvalidApiKeyException();
    }


    private void checkUsernameExistence(String username){
        var checkUser = userRepository.findByUsername(username);
        if(checkUser.isPresent()) throw new UsernameAlreadyExistException();
    }

    private void checkEmailExistence(String email){
        var checkUser = userRepository.findByEmail(email);
        if (checkUser.isPresent()) throw new EmailAlreadyExistException();
    }

    public void setApiKey(String apikey) {
        this.apiKey = apikey;
    }
}