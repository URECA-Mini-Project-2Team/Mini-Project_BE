package kr.co.ureca.service;

import kr.co.ureca.dto.ReservationDto;
import kr.co.ureca.dto.UserDto;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        userRepository.save(user);
    }

    public User checkExistOrNot(ReservationDto reservationDto) throws Exception {
        Optional<User> userOptional = userRepository.findOpByNickName(reservationDto.getNickName());
        User user;
        if (userOptional.isPresent()) { // User가 이미 있음
            user = userOptional.get();
            if (!user.getName().equals(reservationDto.getUserName())) throw  new Exception("이미 사용중인 아이디입니다.");
            if (!user.getPassword().equals(reservationDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");
        } else { // User가 없어서 새로 생성
            user = User.builder()
                    .nickName(reservationDto.getNickName())
                    .name(reservationDto.getNickName())
                    .password(reservationDto.getPassword())
                    .build();
            register(user);
        }

        return user;
    }

    public User checkExistOrNot(UserDto userDto) throws Exception {
        Optional<User> optionalUser = userRepository.findOpByNickName(userDto.getNickName());
        if (optionalUser.isEmpty()) throw new Exception("등록되지 않은 사용자입니다.");
        User user = optionalUser.get();
        if (!user.getPassword().equals(userDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");

        return user;
    }

    public void updateUserStatus(User user) {
        user.updateUserStatus();
        userRepository.save(user);
    }
}
