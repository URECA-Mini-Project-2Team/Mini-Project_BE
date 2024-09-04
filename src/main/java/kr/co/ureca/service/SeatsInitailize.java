package kr.co.ureca.service;

import jakarta.annotation.PostConstruct;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SeatsInitailize {

    private final SeatRepository seatRepository;

    @PostConstruct
    public void init(){
        for(long i = 1; i <= 30; i++){
            Seat seat = new Seat();
            seat.setSeatNo(i);
            seat.setStatus(false);
            seatRepository.save(seat);
        }
    }
}
