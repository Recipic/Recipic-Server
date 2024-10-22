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

    // User
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "U001", "이미 존재하는 이메일입니다."),
    NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "U002", "이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U003", "존재하지 않는 유저입니다."),
    NICKNAME_UNAVAILABLE(HttpStatus.BAD_REQUEST, "U004", "사용할 수 없는 닉네임입니다."),

    // Common
    NOT_FOUND(HttpStatus.BAD_REQUEST, "C001", "리소스를 찾을 수 없습니다!"),
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "C002", "잘못된 입력값입니다!"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "잘못된 HTTP 메서드입니다!"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C004", "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "서버 내부에서 에러가 발생하였습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C006", "잘못된 요청이예요!"),

    // File
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "파일 업로드 중 오류가 발생했어요!"),

    // Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "로그인이 필요해요!");  // 새로운 항목 추가
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}

