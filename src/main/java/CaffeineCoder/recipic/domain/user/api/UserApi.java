package CaffeineCoder.recipic.domain.user.api;


import CaffeineCoder.recipic.domain.authentication.application.AuthService;
import CaffeineCoder.recipic.domain.user.application.UserService;
import CaffeineCoder.recipic.domain.user.dto.UserRequestDto;
import CaffeineCoder.recipic.domain.user.dto.UserResponseDto;
import CaffeineCoder.recipic.domain.authentication.domain.AuthTokensGenerator;
import CaffeineCoder.recipic.domain.user.domain.User;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import com.google.protobuf.Api;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApi {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final AuthService authService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/user")
    public ApiResponse<UserResponseDto> findMemberInfoById() {
        return ApiUtils.success(userService.findMemberInfoById(SecurityUtil.getCurrentMemberId()));
    }

    @PatchMapping("/user/update")
    public ApiResponse<?> findMemberInfoByEmail(
            @RequestPart(value="user", required = false) UserRequestDto userRequestDto,
            @RequestPart(value="profileImage", required=false) MultipartFile profileImage
    ) {
        return ApiUtils.success(userService.updateUser(userRequestDto,profileImage));
    }

    @PostMapping("/user/logout")
    public ApiResponse<?> logout(HttpServletRequest request) {
        return ApiUtils.success(authService.logout(request));
    }

    @DeleteMapping("/user/withdraw")
    public ApiResponse<?> withdraw(HttpServletRequest request, HttpServletResponse response) {
        Long userId = SecurityUtil.getCurrentMemberId();  // 현재 로그인된 유저 ID 가져오기
        userService.withdrawUser(userId);  // 유저 및 연관된 데이터 삭제
        return ApiUtils.success(authService.withdraw(request, response));
    }



}
