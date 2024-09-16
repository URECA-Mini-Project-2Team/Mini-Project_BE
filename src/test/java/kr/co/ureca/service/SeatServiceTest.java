package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
//@Transactional
public class SeatServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ReservationService reservationService;

    private List<SeatDto.RequestDto.ReservationDto> reservationDtoList;

    @Autowired
    private SeatService seatService;

    @BeforeEach
    public void before() {
        System.out.println("begin test");

        for (int i = 1; i <= 1; i++) {
            seatRepository.save(
                    Seat.builder()
                            .seatNo((long) i)
                            .build()
            );
            System.out.println("-------------------- 자리 디비에 넣음 " + i + " -----------------------");
        }

        reservationDtoList = new ArrayList<>();
        for (int i = 1; i <= 80; i++) {
            reservationDtoList.add(
                    SeatDto.RequestDto.ReservationDto.of(1L, "유저" + i, "user" + i, "1234")
            );
        }

        seatRepository.flush();

        System.out.println("================= Data Upload Success ====================");
    }

    @AfterEach
    public void after() {
        System.out.println("end test");
    }

    @Test
    @DisplayName("자리 예약 동시성 테스트")
    public void reservationTest() throws Exception {

        // Given
        int numThreads = 80;
        Long seatNo = 1L;
        Seat seat = seatRepository.findById(seatNo).get();

        CountDownLatch countDownLatch = new CountDownLatch(numThreads);
//        ExecutorService executorService = Executors.newFixedThreadPool(numThreads); // 정적
        ExecutorService executorService = Executors.newCachedThreadPool(); // 동적

        AtomicInteger reservationSuccessCount = new AtomicInteger();
        AtomicInteger reservationFailCount = new AtomicInteger();
        AtomicInteger querySuccessCount = new AtomicInteger();
        AtomicInteger queryFailCount = new AtomicInteger();
        AtomicInteger threadNum = new AtomicInteger();

//       When
        for (int i = 0; i < numThreads; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {

                    if (finalI % 2 == 0) {
                        SeatDto.RequestDto.ReservationDto reservationDto = reservationDtoList.get(finalI);
                        reservationService.reservationSeat(reservationDto);
                        threadNum.set(finalI);
                        reservationSuccessCount.incrementAndGet();
                    } else {
                        List<SeatDto.ResponseDto.SeatListDto> list = seatService.getSeatList();
                        System.out.println("----- 조회 쓰레드 번호 : " + finalI + " 자리 번호 : " + list.get(0).getSeatNo() + ", 자리 상태 : " + list.get(0).getStatus());
                        querySuccessCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    if (finalI % 2 == 0) {
                        reservationFailCount.incrementAndGet();
                    } else queryFailCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        // Then

        System.out.println("예약 성공 쓰레드 번호 : " + threadNum);

        System.out.println("예약 성공 count : " + reservationSuccessCount.get());
        System.out.println("예약 실패 count : " + reservationFailCount.get());
        System.out.println("조회 성공 count : " + querySuccessCount.get());
        System.out.println("조회 실패 count : " + queryFailCount.get());

        assertAll(
                () -> assertThat(reservationSuccessCount.get()).isEqualTo(1),
                () -> assertThat(reservationFailCount.get()).isEqualTo(39),
                () -> assertThat(querySuccessCount.get()).isEqualTo(40),
                () -> assertThat(queryFailCount.get()).isEqualTo(0)
        );
    }
}