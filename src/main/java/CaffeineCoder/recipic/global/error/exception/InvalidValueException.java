package CaffeineCoder.recipic.global.error.exception;

import CaffeineCoder.recipic.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidValueException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidValueException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
