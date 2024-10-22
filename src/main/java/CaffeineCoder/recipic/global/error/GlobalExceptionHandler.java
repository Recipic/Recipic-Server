package CaffeineCoder.recipic.global.error;

import CaffeineCoder.recipic.global.error.exception.BusinessException;
import CaffeineCoder.recipic.global.error.exception.InvalidValueException;
import CaffeineCoder.recipic.global.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
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

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {
        log.error("Exception 발생", e);
        return handleException(e, ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        // 로그 출력
        log.error("Insufficient Authentication: {}", e.getMessage());

        // 적절한 에러 응답 생성
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED); // 401 에러 처리
        return handleException(e, ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage());
    }

    private ResponseEntity<?> handleException(Exception e, ErrorCode errorCode, String message) {
        log.error(message, e);
        return ApiUtils.error(ErrorResponse.of(errorCode, message));
    }

}
