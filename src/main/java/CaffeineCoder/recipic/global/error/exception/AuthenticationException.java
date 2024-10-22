package CaffeineCoder.recipic.global.error.exception;

import CaffeineCoder.recipic.global.error.ErrorCode;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

}
