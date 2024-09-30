package CaffeineCoder.recipic.domain.user.api;

import CaffeineCoder.recipic.domain.user.application.UserService;
import CaffeineCoder.recipic.domain.user.dto.UserRequestDto;
import CaffeineCoder.recipic.domain.user.dto.UserResponseDto;
import CaffeineCoder.recipic.domain.authentication.domain.AuthTokensGenerator;
import CaffeineCoder.recipic.domain.user.domain.User;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
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

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/user")
    public ApiResponse<UserResponseDto> findMemberInfoById() {
        return ApiUtils.success(userService.findMemberInfoById(SecurityUtil.getCurrentMemberId()));
    }

    @PostMapping("/user/update")
    public ApiResponse<?> findMemberInfoByEmail(
            @RequestPart(value="user") UserRequestDto userRequestDto,
            @RequestPart(value="profileImage") MultipartFile profileImage
    ) {
        return ApiUtils.success(userService.updateUser(userRequestDto,profileImage));
    }

}
