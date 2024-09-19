package kr.co.ureca.service;

import kr.co.ureca.dto.SignUpResultDto;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public void registerUser(SignUpResultDto signUpResultDto){
        User user = new User(
                signUpResultDto.getUserName(),
                bCryptPasswordEncoder.encode(signUpResultDto.getPassword()),
                signUpResultDto.getNickName()
        );
        userRepository.save(user);
    }

}
