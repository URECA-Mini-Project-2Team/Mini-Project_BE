package kr.co.ureca.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kr.co.ureca.dto.ReservationRequest;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.exception.CustomException;
import kr.co.ureca.exception.ErrorCode;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ReservationService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void initSeats() {
        if (seatRepository.count() == 0) {
            for (int i = 1; i <= 30; i++) {
                Seat seat = Seat.builder()
                        .seatNo((long) i)
                        .build();
                seatRepository.save(seat);
            }
        }
        log.info("좌석을 생성했습니다.");
    }

    @Transactional
    public Seat reserve(ReservationRequest reservationRequest){

        Seat seat = seatRepository.findBySeatNo(reservationRequest.seatNo())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 좌석입니다. 번호: " + reservationRequest.seatNo()));

        if (seat.getStatus().equals(true)){
            throw new CustomException(ErrorCode.RESERVED_SEAT, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByNickName(reservationRequest.nickname())
                .orElseGet(() -> User.builder()
                        .userName(reservationRequest.userName())
                        .password(reservationRequest.password())
                        .nickName(reservationRequest.nickname())
                        .build()
                );
        if(user.getHasReservation().equals(true)){
            throw new CustomException(ErrorCode.RESERVED_USER, HttpStatus.BAD_REQUEST);
        }

        seat = seat.toBuilder()
                .user(user)
                .status(true)
                .build();

        user = user.toBuilder()
                .seat(seat)
                .build();

        userRepository.save(user);
//        seatRepository.save(seat); 더티체킹

        return seat;

    }

}
