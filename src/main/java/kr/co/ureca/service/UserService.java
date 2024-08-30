package kr.co.ureca.service;

import kr.co.ureca.entity.User;
import kr.co.ureca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity register(User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }


}
