package kr.co.ureca.service;

import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import kr.co.ureca.dto.ReservationRequest;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootTest
@Slf4j
public class ReservationConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    @Transactional
    public void testConcurrentReservations() throws InterruptedException, ExecutionException {
        // 같은 좌석 번호를 10명이 동시에 예약하는 상황을 시뮬레이션
        final int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<String>> tasks = new ArrayList<>();

        long seatNo = 1L;  // 테스트용 좌석 번호
        String userNamePrefix = "User";

        // 10개의 예약 요청을 준비
        for (int i = 0; i < numberOfThreads; i++) {
            final int userIndex = i;
            tasks.add(() -> {
                try {
                    ReservationRequest request = new ReservationRequest(
                            userNamePrefix + userIndex,  // 각기 다른 사용자 이름
                            "password" + userIndex,
                            "nickname" + userIndex,
                            seatNo  // 동일한 좌석 번호에 대한 예약 시도
                    );
                    Seat seat = reservationService.reserve(request);
                    log.info("Success for user {}", userIndex);
                    return "Success";
                } catch (CustomException e) {
                    log.error("Failed for user {}: {}", userIndex, e.getMessage());
                    return "CustomException: " + e.getMessage();
                }
            });
        }

        // 10개의 스레드 실행
        List<Future<String>> results = executorService.invokeAll(tasks);

        // 결과 확인
        int successCount = 0;
        int failureCount = 0;
        int customExceptionCount = 0;


        for (Future<String> result : results) {
            String res = result.get();
            if (res.startsWith("Success")) {
                successCount++;
            } else {
                failureCount++;
                if (res.startsWith("CustomException")) {
                    customExceptionCount++;
                }
            }
            System.out.println(res);
        }

        // 동시성 테스트 결과 검증: 하나의 성공, 나머지 9개는 실패해야 함
        assertEquals(1, successCount);
        assertEquals(9, failureCount);
        assertEquals(9, customExceptionCount);

        executorService.shutdown();
    }
}
