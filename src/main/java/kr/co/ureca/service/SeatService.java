package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

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

}
