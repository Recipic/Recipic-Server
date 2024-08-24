package CaffeineCoder.recipic.global.util;

import CaffeineCoder.recipic.global.error.ErrorResponse;
import CaffeineCoder.recipic.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public class ApiUtils {

    private ApiUtils(){
    }
    public static <T> ApiResponse<T> success(T response) {
        return ApiResponse.create(true, response, null);
    }

    public static ResponseEntity<?> error(ErrorResponse errorResponse) {
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(ApiResponse.create(false, null, errorResponse));
    }
}
