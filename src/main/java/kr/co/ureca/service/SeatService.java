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

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

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

        seat.setStatus(true);
        seat.setUser(user);
        seatRepository.save(seat);

        return seat;
    }

    public Seat deleteSeat(User user) {
        Seat seat = seatRepository.findById(user.getSeat().getSeatId()).get();
        seat.setUser(null);
        seat.setStatus(false);
        seatRepository.save(seat);

        return seat;
    }
}
