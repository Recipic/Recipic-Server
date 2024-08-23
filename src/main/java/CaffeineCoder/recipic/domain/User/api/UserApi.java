package CaffeineCoder.recipic.domain.User.api;

import CaffeineCoder.recipic.domain.authentication.domain.AuthTokensGenerator;
import CaffeineCoder.recipic.domain.User.domain.User;
import CaffeineCoder.recipic.domain.User.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApi {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/user")
    public ResponseEntity<User> findByAccessToken(@RequestParam("accessToken") String accessToken) {
        Long userId = authTokensGenerator.extractUserId(accessToken);
        System.out.println(userId);
        return ResponseEntity.ok(userRepository.findById(userId).get());
    }
}
