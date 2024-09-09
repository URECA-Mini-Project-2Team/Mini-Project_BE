package kr.co.ureca.repository;

import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findOpByUser(User user);
    Seat findBySeatNo(Long seatNo);
}
