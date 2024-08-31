package kr.co.ureca.service;

import kr.co.ureca.dto.ReservationDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.dto.UserDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    @Autowired
    private UserService userService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SeatDto> getSeatList() {
        List<Seat> seats = seatRepository.findAll();
        List<SeatDto> list = new ArrayList<>();
        for (Seat seat : seats) {
            SeatDto seatDto = new SeatDto();
            seatDto.setSeatNo(seat.getSeatNo());
            seatDto.setStatus(seat.getStatus());
            if (seatDto.getStatus()) {
                seatDto.setNickName(userRepository.findById(seat.getUserId()).get().getNickName());
                seatDto.setUserName(userRepository.findById(seat.getUserId()).get().getUserName());
            }
            list.add(seatDto);
        }
        return list;
    }

    public ResponseEntity reservationSeat(ReservationDto reservationDto) throws Exception {
        Optional<User> user = userRepository.findOpByNickName(reservationDto.getNickName());
        User userEntity;
        if (user.isPresent()) { // User가 이미 있음
            userEntity = user.get();
            if (!userEntity.getPassword().equals(reservationDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");
            if (userEntity.getHasReservation()) throw new Exception("이미 선택한 좌석이 있습니다.");
        } else { // User가 없어서 새로 생성
            userEntity = new User();
            userEntity.setNickName(reservationDto.getNickName());
            userEntity.setUserName(reservationDto.getUserName());
            userEntity.setPassword(reservationDto.getPassword());
            userEntity.setHasReservation(false);
            userService.register(userEntity);
        }

        Seat seat = seatRepository.findBySeatNo(reservationDto.getSeatNo());
        if (seat.getStatus()) throw new Exception("이미 예약된 좌석입니다.");

        seat.setStatus(true);
        seat.setUserId(userEntity.getUserId());
        seatRepository.save(seat);

        userEntity.setHasReservation(true);
        userEntity.setSeatId(seat.getSeatId());
        userRepository.save(userEntity);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteSeat(UserDto userDto) throws Exception {
        Optional<User> optionalUser = userRepository.findOpByNickName(userDto.getNickName());
        if (optionalUser.isEmpty()) throw new Exception("등록되지 않은 사용자입니다.");

        User user = optionalUser.get();
        if (!user.getPassword().equals(userDto.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");
        if (!user.getHasReservation()) throw new Exception("등록된 자리가 없습니다.");

        Seat seat = seatRepository.findById(user.getSeatId()).get();
        seat.setUserId(null);
        seat.setStatus(false);
        seatRepository.save(seat);

        user.setHasReservation(false);
        user.setSeatId(null);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
