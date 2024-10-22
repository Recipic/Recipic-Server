package CaffeineCoder.recipic.global.error.exception;

import CaffeineCoder.recipic.global.error.ErrorCode;

public class IllegalException extends BusinessException {
    public IllegalException() {
        super(ErrorCode.BAD_REQUEST);
    }

    public IllegalException(ErrorCode e) {
        super(e);
    }
}
