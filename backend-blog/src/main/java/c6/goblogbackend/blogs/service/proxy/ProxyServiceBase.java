package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.AuthenticationResponse;
import c6.goblogbackend.blogs.exception.ExpiredJWTException;
import c6.goblogbackend.blogs.exception.InvalidApiKeyException;
import c6.goblogbackend.blogs.exception.InvalidJWTException;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class ProxyServiceBase {
    @Setter
    private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    @Autowired
    @Value("${api-key}")
    @Setter
    private String API_KEY;

    @Autowired
    @Value("${AUTH_SERVICE_BASE_URL}")
    @Setter
    private String AUTH_SERVICE_BASE_URL;

    public ResponseEntity<AuthenticationResponse> authenticateJWT(String jwt) throws ExpiredJWTException, InvalidJWTException, InvalidApiKeyException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("X-JWT-TOKEN", jwt);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<AuthenticationResponse> res = null;

        try{
            res = restTemplate.exchange(AUTH_SERVICE_BASE_URL + "/authenticate", HttpMethod.POST, entity, AuthenticationResponse.class);
        } catch(HttpClientErrorException e){
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                if (e.getMessage().contains("The token provided has expired")) {
                    throw new ExpiredJWTException();
                } else if (e.getMessage().contains("Invalid JWT")) {
                    throw new InvalidJWTException();
                }
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new InvalidApiKeyException();
            } else {
                throw e;
            }
        }

        return res;
    }
}
