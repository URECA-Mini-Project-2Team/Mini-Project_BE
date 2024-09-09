package kr.co.ureca.Jungjuhyeon.seat.infrastructure;

import kr.co.ureca.Jungjuhyeon.seat.domain.Seat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SeatJpaRepository extends JpaRepository<Seat,Long> {
    Optional<Seat> findBySeatNo(Long seatNo);

    //EntityGraph+jpql
    @EntityGraph(attributePaths = {"user"})
    @Query("select s from Seat s")
    List<Seat> findSeatEntityGraph();
}

