package vn.demo.nike.shared.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.demo.nike.shared.dto.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    @Test
    void noResourceFound_returnsNotFoundResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        NoResourceFoundException exception =
                new NoResourceFoundException(HttpMethod.GET, ".well-known/appspecific/com.chrome.devtools.json");

        ResponseEntity<ErrorResponse> response = handler.noResourceFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getBody().message())
                .isEqualTo("No static resource .well-known/appspecific/com.chrome.devtools.json.");
    }
}
