package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

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
                                .userName(user.getName())
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Seat reservationSeat(Long seatNo, User user) throws Exception {
        if (user.getStatus()) throw new Exception("이미 등록된 자리가 있습니다.");
        Optional<Seat> seatOptional = seatRepository.findOpBySeatNo(seatNo);
        if (seatOptional.isEmpty()) throw new Exception("존재하지 않은 자리입니다.");
        Seat seat = seatOptional.get();
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
