package CaffeineCoder.recipic.login;

import CaffeineCoder.recipic.global.error.ErrorCode;
import CaffeineCoder.recipic.global.error.exception.IllegalException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-illegal-exception")
public class TestExceptionController {

    @GetMapping
    public void throwIllegalException() {
        throw new IllegalException(ErrorCode.BAD_REQUEST); // IllegalException을 강제로 발생
    }
}