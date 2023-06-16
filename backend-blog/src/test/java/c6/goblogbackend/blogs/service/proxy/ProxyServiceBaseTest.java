package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.AuthenticationResponse;
import c6.goblogbackend.blogs.exception.ExpiredJWTException;
import c6.goblogbackend.blogs.exception.InvalidApiKeyException;
import c6.goblogbackend.blogs.exception.InvalidJWTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

class ProxyServiceBaseTest {

    private ProxyServiceBase proxyService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        proxyService = new ProxyServiceBase();
        proxyService.setRestTemplate(restTemplate);
    }

    @Test
    void authenticateJWT_ValidToken_ReturnsResponseEntity() throws ExpiredJWTException, InvalidJWTException, InvalidApiKeyException {
        // Arrange
        String jwt = "valid-jwt";
        String baseUrl = "http://example.com";
        String apiKey = "api-key";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("X-JWT-TOKEN", jwt);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<AuthenticationResponse> expectedResponse = new ResponseEntity<>(new AuthenticationResponse(), HttpStatus.OK);

        when(restTemplate.exchange(baseUrl + "/authenticate", HttpMethod.POST, entity, AuthenticationResponse.class))
                .thenReturn(expectedResponse);

        proxyService.setAUTH_SERVICE_BASE_URL(baseUrl);
        proxyService.setAPI_KEY(apiKey);

        // Act
        ResponseEntity<AuthenticationResponse> actualResponse = proxyService.authenticateJWT(jwt);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate, times(1)).exchange(baseUrl + "/authenticate", HttpMethod.POST, entity, AuthenticationResponse.class);
    }


    @Test
    void authenticateJWT_ExpiredToken_ThrowsExpiredJWTException() {
        // Arrange
        String jwt = "expired-jwt";
        String baseUrl = "http://example.com";
        String apiKey = "api-key";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("X-JWT-TOKEN", jwt);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "The token provided has expired");

        when(restTemplate.exchange((baseUrl + "/authenticate"), (HttpMethod.POST), (entity), (AuthenticationResponse.class)))
                .thenThrow(exception);

        proxyService.setAUTH_SERVICE_BASE_URL(baseUrl);
        proxyService.setAPI_KEY(apiKey);

        // Act & Assert
        assertThrows(ExpiredJWTException.class, () -> proxyService.authenticateJWT(jwt));
        verify(restTemplate, times(1)).exchange((baseUrl + "/authenticate"), (HttpMethod.POST), (entity), (AuthenticationResponse.class));
    }

    @Test
    void authenticateJWT_InvalidToken_ThrowsInvalidJWTException() {
        // Arrange
        String jwt = "invalid-jwt";
        String baseUrl = "http://example.com";
        String apiKey = "api-key";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("X-JWT-TOKEN", jwt);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid JWT");

        when(restTemplate.exchange((baseUrl + "/authenticate"), (HttpMethod.POST), (entity), (AuthenticationResponse.class)))
                .thenThrow(exception);

        proxyService.setAUTH_SERVICE_BASE_URL(baseUrl);
        proxyService.setAPI_KEY(apiKey);

        // Act & Assert
        assertThrows(InvalidJWTException.class, () -> proxyService.authenticateJWT(jwt));
        verify(restTemplate, times(1)).exchange((baseUrl + "/authenticate"), (HttpMethod.POST), (entity), (AuthenticationResponse.class));
    }

    @Test
    void authenticateJWT_InvalidApiKey_ThrowsInvalidApiKeyException() {
        // Arrange
        String jwt = "valid-jwt";
        String baseUrl = "http://example.com";
        String apiKey = "invalid-api-key";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("X-JWT-TOKEN", jwt);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.FORBIDDEN);

        when(restTemplate.exchange((baseUrl + "/authenticate"), (HttpMethod.POST), (entity), (AuthenticationResponse.class)))
                .thenThrow(exception);

        proxyService.setAUTH_SERVICE_BASE_URL(baseUrl);
        proxyService.setAPI_KEY(apiKey);

        // Act & Assert
        assertThrows(InvalidApiKeyException.class, () -> proxyService.authenticateJWT(jwt));
        verify(restTemplate, times(1)).exchange((baseUrl + "/authenticate"), (HttpMethod.POST), (entity), (AuthenticationResponse.class));
    }

    @Test
    void authenticateJWT_HttpError_ThrowsHttpClientErrorException() {
        // Arrange
        String jwt = "valid-jwt";
        String baseUrl = "http://example.com";
        String apiKey = "api-key";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("X-JWT-TOKEN", jwt);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

        when(restTemplate.exchange((baseUrl + "/authenticate"), (HttpMethod.POST), (entity), (AuthenticationResponse.class)))
                .thenThrow(exception);

        proxyService.setAUTH_SERVICE_BASE_URL(baseUrl);
        proxyService.setAPI_KEY(apiKey);

        // Act & Assert
        assertThrows(HttpClientErrorException.class, () -> proxyService.authenticateJWT(jwt));
        verify(restTemplate, times(1)).exchange((baseUrl + "/authenticate"), (HttpMethod.POST), (entity), (AuthenticationResponse.class));
    }
}
