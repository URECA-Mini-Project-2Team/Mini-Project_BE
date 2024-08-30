package kr.co.ureca.repository;

import kr.co.ureca.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Seat findBySeatNo(Long seatNo);
}
