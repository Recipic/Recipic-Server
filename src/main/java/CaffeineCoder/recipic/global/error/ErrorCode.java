package CaffeineCoder.recipic.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    // Data
    DATA_IS_NOT_EXIST(HttpStatus.BAD_REQUEST,"D01","데이터가 더이상 존재하지 않습니다"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "D02", "잘못된 입력 값입니다."),


    // Common
    NOT_FOUND(HttpStatus.BAD_REQUEST, "C001", "Not Found resource"),
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "C002", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "잘못된 HTTP 메서드입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C004", "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "서버 내부에서 에러가 발생하였습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C006", "잘못된 요청이예요!"),

    // Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "로그인이 필요해요!");  // 새로운 항목 추가
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}

