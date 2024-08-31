package kr.co.ureca.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kr.co.ureca.dto.DeleteReservationRequest;
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

        user = user.toBuilder()
                .hasReservation(true)
                .build();

        seat = seat.toBuilder()
                .user(user)
                .status(true)
                .build();

        userRepository.save(user);
        seatRepository.save(seat);

        return seat;

    }

    @Transactional
    public Seat deleteReservation(DeleteReservationRequest deleteReservationRequest){
        Seat seat = seatRepository.findBySeatNo(deleteReservationRequest.seatNo())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 좌석입니다. 좌석번호: " + deleteReservationRequest.seatNo()));
        User user = seat.getUser();
        String nickName = deleteReservationRequest.nickname();
        String userName = deleteReservationRequest.userName();
        String password = deleteReservationRequest.password();

        if(seat.getStatus().equals(true)) {
            if (user.getNickName().equals(nickName)
                    && user.getUserName().equals(userName)
                    && user.getPassword().equals(password)) {
                seat = seat.toBuilder()
                        .user(null)
                        .status(false)
                        .build();
                user = user.toBuilder()
                        .hasReservation(false)
                        .build();

                seatRepository.save(seat);
                userRepository.save(user);
            } else {
                throw new CustomException(ErrorCode.UNAUTHORIZED_USER, HttpStatus.BAD_REQUEST);
            }
        }else{
            throw new CustomException(ErrorCode.UNVALID_DELETE_REQUEST, HttpStatus.BAD_REQUEST);
        }

        return seat;
    }
    
}
