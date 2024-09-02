package kr.co.ureca.service;

import kr.co.ureca.dto.ReservationDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.dto.UserDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void register(User user) {
        userRepository.save(user);
    }

    public SeatDto setSeatDto(SeatDto seatDto, Seat seat) {
        seatDto.setNickName(userRepository.findById(seat.getUserId()).get().getNickName());
        seatDto.setUserName(userRepository.findById(seat.getUserId()).get().getUserName());
        return seatDto;
    }

    public User checkExistOrNot(ReservationDto reservationDto) throws Exception {
        Optional<User> userOptional = userRepository.findOpByNickName(reservationDto.getNickName());
        User user;
        if (userOptional.isPresent()) { // User가 이미 있음
            user = userOptional.get();
            if (!user.getPassword().equals(reservationDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");
            if (user.getHasReservation()) throw new Exception("이미 선택한 좌석이 있습니다.");
        } else { // User가 없어서 새로 생성
            user = new User();
            user.setNickName(reservationDto.getNickName());
            user.setUserName(reservationDto.getUserName());
            user.setPassword(reservationDto.getPassword());
            user.setHasReservation(false);
            register(user);
        }

        return user;
    }

    public User checkExistOrNot(UserDto userDto) throws Exception {
        Optional<User> optionalUser = userRepository.findOpByNickName(userDto.getNickName());
        if (optionalUser.isEmpty()) throw new Exception("등록되지 않은 사용자입니다.");
        User user = optionalUser.get();
        if (!user.getPassword().equals(userDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");
        if (!user.getHasReservation()) throw new Exception("등록된 자리가 없습니다.");

        return user;
    }

    public void setUserSeat(User user, Seat seat) {
        if (seat.getStatus()) {
            user.setHasReservation(true);
            user.setSeatId(seat.getSeatId());
            userRepository.save(user);
        } else {
            user.setHasReservation(false);
            user.setSeatId(null);
            userRepository.save(user);
        }
    }


}
