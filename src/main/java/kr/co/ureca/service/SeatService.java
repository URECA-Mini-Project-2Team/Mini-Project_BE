package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
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
            seatDto.setSeatId(seat.getSeatId());
            seatDto.setSeatNo(seat.getSeatNo());
            seatDto.setUserId(seat.getUserId());
            seatDto.setStatus(seat.getStatus());
            list.add(seatDto);
        }
        return list;
    }

}
