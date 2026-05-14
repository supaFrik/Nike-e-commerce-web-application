package vn.demo.nike.shared.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.demo.nike.features.admin.product.exception.InvalidProductColorException;
import vn.demo.nike.features.admin.product.exception.InvalidSalePriceException;
import vn.demo.nike.features.admin.product.exception.InvalidSizeException;
import vn.demo.nike.features.catalog.cart.exception.*;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.product.exception.InvalidProductStatusException;
import vn.demo.nike.features.catalog.product.exception.ProductNotFoundException;
import vn.demo.nike.features.checkout.exception.InvalidCheckoutRequestException;
import vn.demo.nike.features.order.exception.InvalidOrderStateException;
import vn.demo.nike.features.order.exception.OrderIdAndUserIdNotFoundException;
import vn.demo.nike.features.payment.exception.InvalidPaymentMethodException;
import vn.demo.nike.shared.dto.ErrorResponse;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Request payload invalid: " + ex.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(InvalidCheckoutRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCheckoutRequestException(InvalidCheckoutRequestException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Checkout request error: " + e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(Exception e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Database error: " + e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public Object productNotFoundException(ProductNotFoundException e, HttpServletRequest request) {
        if (wantsHtml(request)) {
            return notFoundView();
        }

        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    @ExceptionHandler(InvalidProductStatusException.class)
    public ResponseEntity<ErrorResponse> invalidProductStatusException(InvalidProductStatusException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(InvalidProductColorException.class)
    public ResponseEntity<ErrorResponse> invalidProductColorException(InvalidProductColorException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(InvalidSalePriceException.class)
    public ResponseEntity<ErrorResponse> invalidSalePriceException(InvalidSalePriceException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(InvalidSizeException.class)
    public ResponseEntity<ErrorResponse> invalidSizeException(InvalidSizeException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler({
            InvalidCartQuantityException.class,
            InactiveVariantException.class,
            InsufficientStockException.class
    })
    public ResponseEntity<ErrorResponse> cartBadRequest(RuntimeException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler({
            CartItemNotFoundException.class,
            VariantNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> cartNotFound(RuntimeException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    @ExceptionHandler(UnauthenticatedUserException.class)
    public ResponseEntity<ErrorResponse> unauthenticatedUser(UnauthenticatedUserException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object noResourceFound(NoResourceFoundException e, HttpServletRequest request) {
        if (wantsHtml(request)) {
            return notFoundView();
        }

        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    @ExceptionHandler(OrderIdAndUserIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> orderIdAndUserIdNotFound(OrderIdAndUserIdNotFoundException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponse> invalidOrderStateException(InvalidOrderStateException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    @ExceptionHandler(InvalidPaymentMethodException.class)
    public ResponseEntity<ErrorResponse> invalidPaymentMethodException(InvalidPaymentMethodException e) {
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        e.printStackTrace();
        ErrorResponse errorBody = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later.",
                Instant.now().toEpochMilli()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    private boolean wantsHtml(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains(MediaType.TEXT_HTML_VALUE);
    }

    private ModelAndView notFoundView() {
        ModelAndView modelAndView = new ModelAndView("user/error/404");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
