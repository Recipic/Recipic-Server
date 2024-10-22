package CaffeineCoder.recipic.global.error.exception;

import CaffeineCoder.recipic.global.error.ErrorCode;

public class InvalidUserException extends BusinessException {
    public InvalidUserException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
