package CaffeineCoder.recipic.global.error;

import CaffeineCoder.recipic.global.error.exception.BusinessException;
import CaffeineCoder.recipic.global.error.exception.InvalidValueException;
import CaffeineCoder.recipic.global.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.security.sasl.AuthenticationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * javax.validation.Valid 또는 @Validated binding error가 발생할 경우
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return handleException(e, ErrorCode.INVALID_VALUE, e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    /**
     * @RequestParam binding error가 발생할 경우
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<?> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return handleException(e, ErrorCode.INVALID_VALUE, e.getMessage());
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return handleException(e, ErrorCode.INVALID_VALUE, e.getMessage());
    }

    /**
     * 지원하지 않은 HTTP method 호출할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return handleException(e, ErrorCode.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED.getMessage());
    }


    @ExceptionHandler(InvalidValueException.class)
    private ResponseEntity<?> handleInvalidValueException(final InvalidValueException e){
        return handleException(e, e.getErrorCode(), e.getMessage());
    }



    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<?> handleBusinessException(final BusinessException e) {
        return handleException(e, e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> handleAuthenticationException(final AuthenticationException e) {
        log.error("AuthenticationException 발생", e);
        return handleException(e, ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        // 로그 출력
        log.error("Insufficient Authentication: {}", e.getMessage());

        // 적절한 에러 응답 생성
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED); // 401 에러 처리
        return handleException(e, ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {
        log.error("Exception 발생", e);
        return handleException(e, ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    private ResponseEntity<?> handleException(Exception e, ErrorCode errorCode, String message) {
        log.error(message, e);
        return ApiUtils.error(ErrorResponse.of(errorCode, message));
    }

}
