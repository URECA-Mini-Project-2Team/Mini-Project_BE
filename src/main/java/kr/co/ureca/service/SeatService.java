package kr.co.ureca.service;

import kr.co.ureca.dto.ReservationDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<SeatDto> getSeatList() {
        List<Seat> seats = seatRepository.findAll();
        List<SeatDto> list = new ArrayList<>();
        for (Seat seat : seats) {
            SeatDto seatDto = new SeatDto();
            seatDto.setSeatNo(seat.getSeatNo());
            seatDto.setStatus(seat.getStatus());
            if (seatDto.getStatus()) {
                seatDto.setNickName(seat.getUser().getNickName());
                seatDto.setUserName(seat.getUser().getUserName());
            }
            list.add(seatDto);
        }
        return list;
    }

    public Seat reservationSeat(ReservationDto reservationDto, User user) throws Exception {
        Seat seat = seatRepository.findBySeatNo(reservationDto.getSeatNo());
        if (seat.getStatus()) throw new Exception("이미 예약된 좌석입니다.");

        seat.updateSeatReservation(user, true);
        seatRepository.save(seat);

        return seat;
    }

    public Seat deleteSeat(User user) throws Exception {
        Optional<Seat> seatOptional = seatRepository.findById(user.getSeat().getSeatId());

        if (seatOptional.isPresent()) {
            Seat seat = seatOptional.get();
            seat.updateSeatReservation(null, false);
            seatRepository.save(seat);

            return seat;
        }

        else throw new Exception("자리가 존재하지 않습니다.");
    }
}
