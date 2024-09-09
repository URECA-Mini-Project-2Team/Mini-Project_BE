package kr.co.ureca;

import kr.co.ureca.Jungjuhyeon.global.exception.BusinessException;
import kr.co.ureca.Jungjuhyeon.seat.application.SeatService;
import kr.co.ureca.Jungjuhyeon.user.domin.Enum.ReservationStatus;
import kr.co.ureca.Jungjuhyeon.seat.domain.Seat;
import kr.co.ureca.Jungjuhyeon.seat.infrastructure.SeatJpaRepository;
import kr.co.ureca.Jungjuhyeon.seat.presentation.dto.SeatDto;
import kr.co.ureca.Jungjuhyeon.user.application.UserService;
import kr.co.ureca.Jungjuhyeon.user.domin.User;
import kr.co.ureca.Jungjuhyeon.user.infrastructure.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
//@Rollback(value = false)
public class SeatServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    private final Long seatNo = 9L; // 테스트할 좌석 번호

    @Test
    public void testConcurrentSeatReservation() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            String email = "user" + i + "@example.com";
            String password = "password" + i;

            int finalI = i;
            executorService.submit(() -> {
                try {
                    SeatDto.Request.reservationDto request = SeatDto.Request.reservationDto.of("user" + finalI, email, password, seatNo);
                    User user = userService.login(request);
                    seatService.reservation(seatNo, user);
                } catch (BusinessException e) {
                    System.out.println("User " + finalI + " failed to reserve the seat: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(10, TimeUnit.SECONDS); // 시간 여유를 두어 충분히 대기

        Seat reservedSeat = seatJpaRepository.findBySeatNo(seatNo).orElseThrow();
        // 예약된 좌석의 상태를 확인
        assertThat(reservedSeat.getUser().getStatus()).isEqualTo(ReservationStatus.RESERVED);


        long reservedCount = userJpaRepository.findAll().stream()
                .filter(user -> user.getSeat() != null && user.getSeat().getSeatNo().equals(seatNo))
                .count();

        // 오직 한 명만이 좌석을 예약해야 함
        assertThat(reservedCount).isEqualTo(1);
    }

}



