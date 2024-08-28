package kr.co.ureca.service;

import jakarta.annotation.PostConstruct;
import kr.co.ureca.Entity.Seat;
import kr.co.ureca.Repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private SeatRepository seatRepository;

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
    }

}
