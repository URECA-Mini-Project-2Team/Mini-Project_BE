package kr.co.ureca.service;

import kr.co.ureca.dto.ReservationDeleteDto;
import kr.co.ureca.dto.ReservationRequestDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final SeatRepository seatRepository;


    public List<SeatDto> getAllSeats() {
        List<Seat> seats = seatRepository.findAll();

        return seats.stream().map(seat -> {
            SeatDto dto = new SeatDto();
            dto.setSeatNo(seat.getId());
            dto.setStatus(seat.isStatus());
            if(seat.getUser() != null){
                dto.setNickName(seat.getUser().getNickName());
                dto.setUserName(seat.getUser().getUserName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

//    public User saveUser(UserInfoDto userInfoDto){
//        User user = new User();
//        user.setUserName(userInfoDto.getUserName());
//        user.setPassword(userInfoDto.getPassword());
//        user.setNickName(userInfoDto.getNickName());
//        user.setHasReservation(false);
//        return userRepository.save(user);
//    }
    public boolean selectSeatToUser(ReservationRequestDto reservationRequestDto) {
        Seat seat = seatRepository.findById(reservationRequestDto.getSeatNo())
                .orElse(null);

        if(seat == null || seat.isStatus()){
            return false;
        }

        User user = new User();
        user.setUserName(reservationRequestDto.getUserName());
        user.setNickName(reservationRequestDto.getNickName());
        user.setPassword(reservationRequestDto.getPassword());
        user.setHasReservation(true);
        user.setSeat(seat);
        seat.setStatus(true);

        userRepository.save(user);
        seatRepository.save(seat);

        return true;

    }

    public boolean deleteSeat(ReservationDeleteDto reservationDeleteDto){
        /*
        예약된 좌석을 클릭했을 때, 사용자 정보 일부를 입력 받아 해당 좌석의 예약자와 일치 여부를 판단
        -> 일치하면 예약 취소
        -> 일치하지 않으면 --
         */
        Optional<User> userOpt = userRepository.findByNickName(reservationDeleteDto.getNickName());

        if(userOpt.isPresent()){
            User user = userOpt.get();

            if(user.getPassword().equals(reservationDeleteDto.getPassword()) && user.isHasReservation()){

                Seat seat = seatRepository.findById(user.getSeat().getId())
                        .orElse(null);

                seat.setUser(null);
                seat.setStatus(false);
                seatRepository.save(seat);

                user.setHasReservation(false);
                user.setSeat(null);
                userRepository.save(user);

                return true;
            }
        }

        return false;
    }
}
