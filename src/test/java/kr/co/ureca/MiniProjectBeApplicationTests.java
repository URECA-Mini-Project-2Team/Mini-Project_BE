package kr.co.ureca;

import kr.co.ureca.dto.ReservationRequestDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import kr.co.ureca.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest

class MiniProjectBeApplicationTests {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Test
//    @Transactional 하나의 트랜잭션으로 묶어서 SeatNo가 db에 저장되지 않아 Seat not found가 출력됨 -> 각 작업이 db에 즉시 반영되게 @Transactional을 사용하지않음
    public void test() throws InterruptedException{
        int thread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(thread);
        CountDownLatch lantch = new CountDownLatch(thread);

        Seat seat = seatRepository.findById(1L).orElseThrow(() -> new RuntimeException("Seat not found"));
        for(int i = 0; i < thread; i++){
            int num = i;
            executorService.execute(() -> {
                try {
                    ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
                    reservationRequestDto.setUserName("User"+num);
                    reservationRequestDto.setNickName("Nick"+num);
                    reservationRequestDto.setPassword("pass");
                    reservationRequestDto.setSeatNo(seat.getId());

                    boolean success = reservationService.selectSeatToUser(reservationRequestDto);
                    if(success){
                        System.out.println("Thread" + num + "succeeded");
                    }else{
                        System.out.println("Thread" + num + "failed");
                    }
                } finally {
                    lantch.countDown();
                }
            });
        }
        lantch.await();
        executorService.shutdown();

        Seat finalSeat = seatRepository.findById(seat.getId()).orElse(null);
        assert finalSeat != null;
        assert finalSeat.getUser().getStatus();
        assert userRepository.count()==1;

    }
//    @DisplayName("30개 좌석에 30명")
////    @Transactional 하나의 트랜잭션으로 묶어서 SeatNo가 db에 저장되지 않아 Seat not found가 출력됨 -> 각 작업이 db에 즉시 반영되게 @Transactional을 사용하지않음
//    public void testAll() throws InterruptedException{
//        int thread = 30;
//        ExecutorService executorService = Executors.newFixedThreadPool(thread);
//        CountDownLatch lantch = new CountDownLatch(thread);
//
//        List<Seat> seats = seatRepository.findAll();
//
//        for(int i = 0; i < thread; i++){
//            int num = i;
//            Seat seat = seats.get(num);
//            executorService.execute(() -> {
//                try {
//                    ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
//                    reservationRequestDto.setUserName("User"+num);
//                    reservationRequestDto.setNickName("Nick"+num);
//                    reservationRequestDto.setPassword("pass");
//                    reservationRequestDto.setSeatNo(seat.getId());
//
//                    boolean success = reservationService.selectSeatToUser(reservationRequestDto);
//                    if(success){
//                        System.out.println("Thread" + num + "succeeded");
//                    }else{
//                        System.out.println("Thread" + num + "failed");
//                    }
//                } finally {
//                    lantch.countDown();
//                }
//            });
//        }
//        lantch.await();
//        executorService.shutdown();
//
//        for(Seat seat : seats) {
//            Seat finalSeat = seatRepository.findById(seat.getId()).orElse(null);
//            assert finalSeat != null;
//            assert finalSeat.getStatus();
//        }
//
//        assert userRepository.count() == thread;
//    }

}
