package kr.co.ureca.service;

import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private SeatService seatService;

    @Autowired
    private UserService userService;
    @Autowired
    private SeatRepository seatRepository;

    private List<User> userList;

    @BeforeEach
    public void before() {
        System.out.println("begin test");

        for (int i = 1; i <= 30; i++) {
            seatRepository.save(
                    Seat.builder()
                            .seatNo((long) i)
                            .build()
            );
            System.out.println("-------------------- 자리 디비에 넣음 " + i + " -----------------------");
        }

        userList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            userRepository.save(
                    User.builder()
                            .nickName("user" + i)
                            .name("유저" + i)
                            .password("1234")
                            .build()
            );
            Optional<User> userOptional = userRepository.findById((long) i);
            if (userOptional.isPresent()) {
                userList.add(userOptional.get());
                System.out.println("-------------------- 유저 리스트에 넣음 " + i + " -----------------------");
            }
        }

        userRepository.flush();
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
        int numThreads = 10;
        Long seatNo = 1L;

        CountDownLatch countDownLatch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

//       When
        for (int i = 0; i < numThreads; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    User user = userList.get(finalI);

                    seatService.reservationSeat(seatNo, user);//userList.get(finalI));
                    userService.updateUserStatus(user);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.getAndIncrement();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        System.out.println("success count : " + successCount.get());
        System.out.println("fail count : " + failCount.get());

        // Then
        assertAll(
                () -> assertThat(successCount.get()).isEqualTo(1),
                () -> assertThat(failCount.get()).isEqualTo(9)
        );
    }

}
