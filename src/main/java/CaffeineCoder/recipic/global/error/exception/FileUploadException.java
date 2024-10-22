package CaffeineCoder.recipic.global.error.exception;

import CaffeineCoder.recipic.global.error.ErrorCode;

public class FileUploadException extends BusinessException {
    public FileUploadException() {
        super(ErrorCode.FILE_UPLOAD_ERROR);
    }
}
