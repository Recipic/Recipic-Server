package CaffeineCoder.recipic.error;

import CaffeineCoder.recipic.global.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIllegalException() throws Exception {
        // 특정 요청에서 IllegalException이 발생하도록 설정
        mockMvc.perform(get("/test-illegal-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 400 상태 코드 기대
                .andExpect(jsonPath("$.errorResponse.status").value(400) )// 변경된 JSON 경로
                .andExpect(jsonPath("$.errorResponse.message").value(ErrorCode.BAD_REQUEST.getMessage())); // 변경된 JSON 경로
    }
}
