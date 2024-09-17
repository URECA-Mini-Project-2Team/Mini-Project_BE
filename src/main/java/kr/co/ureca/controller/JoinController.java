package kr.co.ureca.controller;

import kr.co.ureca.dto.SignUpResultDto;
import kr.co.ureca.entity.User;
import kr.co.ureca.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;
//    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody SignUpResultDto signUpResultDto) {
        userService.registerUser(signUpResultDto);
        return ResponseEntity.ok("User registered successfully");
    }

}
