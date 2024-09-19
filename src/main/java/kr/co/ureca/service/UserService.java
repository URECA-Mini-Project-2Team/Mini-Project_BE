package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.dto.SignUpDto;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public User checkUserExistOrNot(String userName) throws Exception {
        Optional<User> userOpt = userRepository.findByName(userName);
        if (userOpt.isEmpty()) throw new UsernameNotFoundException("User not found with username: " + userName);
        return userOpt.get();
    }
//    @Transactional(propagation = Propagation.REQUIRED)
//    public User checkUserExistOrNot(SeatDto.RequestDto.ReservationDto reservationDto) throws Exception {
//        Optional<User> userOpt = userRepository.findOpByNickName(reservationDto.getNickName());
//        if(userOpt.isEmpty()) throw new UsernameNotFoundException("User not found with userNickName");
//
//        User user = userOpt.get();
//        if (!user.getName().equals(reservationDto.getUserName())) throw  new Exception("이미 사용중인 아이디입니다.");
//        if (!user.getPassword().equals(reservationDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");
//
//        return user;
//    }

    public User checkUserExistOrNot(SeatDto.RequestDto.DeleteDto deleteDto) throws Exception {
        Optional<User> optionalUser = userRepository.findOpByNickName(deleteDto.getNickName());
        if (optionalUser.isEmpty()) throw new Exception("등록되지 않은 사용자입니다.");
        User user = optionalUser.get();
        System.out.println(user);
        //순서 중요함
        if(!passwordEncoder.matches(deleteDto.getPassword(),user.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");
        if(!user.getStatus()) throw new Exception("예약된 좌석이 없습니다.");
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserStatus(User user) {
        user.updateUserStatus();
        //예약 취소 시 false로 안바뀌는 문제 -> 엔티티 명시적으로 저장
        userRepository.save(user);
    }

    public User createUser(SignUpDto signUpDto){
        User user = User.createUser(
                signUpDto.getUserName(),
                signUpDto.getNickName(),
                bCryptPasswordEncoder.encode(signUpDto.getPassword())
        );
        return userRepository.save(user);
    }

}
