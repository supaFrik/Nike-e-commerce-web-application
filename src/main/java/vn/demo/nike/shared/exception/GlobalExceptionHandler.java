package vn.demo.nike.shared.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
import vn.demo.nike.infras.payment.vnpay.exception.InvalidPaymentMethodException;
import vn.demo.nike.shared.dto.ErrorResponse;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        return error(HttpStatus.BAD_REQUEST, "Request payload invalid: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidCheckoutRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCheckoutRequestException(InvalidCheckoutRequestException e) {
        return error(HttpStatus.BAD_REQUEST, "Checkout request error: " + e.getMessage());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(Exception e) {
        log.warn("Data integrity violation", e);
        return error(HttpStatus.BAD_REQUEST, "Request violates data integrity rules.");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public Object productNotFoundException(ProductNotFoundException e, HttpServletRequest request) {
        if (wantsHtml(request)) return notFoundView();
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidProductStatusException.class)
    public ResponseEntity<ErrorResponse> invalidProductStatusException(InvalidProductStatusException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidProductColorException.class)
    public ResponseEntity<ErrorResponse> invalidProductColorException(InvalidProductColorException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidSalePriceException.class)
    public ResponseEntity<ErrorResponse> invalidSalePriceException(InvalidSalePriceException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidSizeException.class)
    public ResponseEntity<ErrorResponse> invalidSizeException(InvalidSizeException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({InvalidCartQuantityException.class, InactiveVariantException.class, InsufficientStockException.class})
    public ResponseEntity<ErrorResponse> cartBadRequest(RuntimeException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({CartItemNotFoundException.class, VariantNotFoundException.class})
    public ResponseEntity<ErrorResponse> cartNotFound(RuntimeException e) {
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UnauthenticatedUserException.class)
    public ResponseEntity<ErrorResponse> unauthenticatedUser(UnauthenticatedUserException e) {
        return error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object noResourceFound(NoResourceFoundException e, HttpServletRequest request) {
        if (wantsHtml(request)) return notFoundView();
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(OrderIdAndUserIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> orderIdAndUserIdNotFound(OrderIdAndUserIdNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponse> invalidOrderStateException(InvalidOrderStateException e) {
        return error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(InvalidPaymentMethodException.class)
    public ResponseEntity<ErrorResponse> invalidPaymentMethodException(InvalidPaymentMethodException e) {
        return error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("Unhandled application exception", e);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message, Instant.now().toEpochMilli()));
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
