package CaffeineCoder.recipic.global.error.exception;

import CaffeineCoder.recipic.global.error.ErrorCode;

public class InvalidUserException extends BusinessException {
    public InvalidUserException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
