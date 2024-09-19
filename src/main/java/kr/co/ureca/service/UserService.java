package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User checkUserExistOrNot(SeatDto.RequestDto.ReservationDto reservationDto) throws Exception {
        User user = userRepository.findOpByNickName(reservationDto.getNickName())
                .orElseGet(()-> createUser(reservationDto));
        if (!user.getName().equals(reservationDto.getUserName())) throw  new Exception("이미 사용중인 아이디입니다.");
        if (!user.getPassword().equals(reservationDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");

        return user;
    }

    public User checkUserExistOrNot(SeatDto.RequestDto.DeleteDto deleteDto) throws Exception {
        Optional<User> optionalUser = userRepository.findOpByNickName(deleteDto.getNickName());
        if (optionalUser.isEmpty()) throw new Exception("등록되지 않은 사용자입니다.");
        User user = optionalUser.get();
        if (!user.getPassword().equals(deleteDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");

        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserStatus(User user) {
        user.updateUserStatus();
    }

    public User createUser(SeatDto.RequestDto.ReservationDto reservationDto){
        User user = User.createUser(reservationDto.getUserName(), reservationDto.getNickName(), reservationDto.getPassword());
        return userRepository.save(user);
    }

}
