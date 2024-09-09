package kr.co.ureca.service;

import kr.co.ureca.dto.ReservationDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final SeatRepository seatRepository;


    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<SeatDto> getSeatList() {
        List<Seat> seats = seatRepository.findAll();
        return seats.stream()
                .map(seat -> {
                    User user = seat.getUser();
                    if (user != null) {
                        return SeatDto.builder()
                                .seatNo(seat.getSeatNo())
                                .status(true)
                                .nickName(user.getNickName())
                                .userName(user.getUserName())
                                .build();
                    } else {
                        return SeatDto.builder()
                                .seatNo(seat.getSeatNo())
                                .status(false)
                                .nickName(null)
                                .userName(null)
                                .build();
                    }
                }).collect(Collectors.toList());
    }

//    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Seat reservationSeat(ReservationDto reservationDto, User user) throws Exception {
        Optional<Seat> seatOptional = seatRepository.findOpByUser(user);
        if (seatOptional.isPresent()) {
            throw new Exception("이미 등록된 자리가 있습니다.");
        }
        Seat seat = seatRepository.findBySeatNo(reservationDto.getSeatNo());
        if (seat.getUser() != null) throw new Exception("이미 예약된 좌석입니다.");

        seat.updateUser(user);
        seatRepository.save(seat);

        return seat;
    }

    public Seat deleteSeat(User user) throws Exception {
        Optional<Seat> seatOptional = seatRepository.findOpByUser(user);

        if (seatOptional.isPresent()) {
            Seat seat = seatOptional.get();
            seat.updateUser(null);
            seatRepository.save(seat);

            return seat;
        }
        else throw new Exception("등록된 자리가 없습니다.");
    }
}
