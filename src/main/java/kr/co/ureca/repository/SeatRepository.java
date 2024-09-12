package kr.co.ureca.repository;

import jakarta.persistence.LockModeType;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findOpByUser(User user);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Seat> findOpBySeatNo(Long seatNo);
}
