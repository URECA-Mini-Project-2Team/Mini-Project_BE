package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final SeatService seatService;

    private final UserService userService;

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public void reservationSeat(SeatDto.RequestDto.ReservationDto reservationDto) throws Exception {
        Seat seat = seatService.checkSeatExistsOrNot(reservationDto.getSeatNo());
        User user = userService.checkUserExistOrNot(reservationDto);
        seatService.reservationSeat(seat, user);
        userService.updateUserStatus(user);
//        goSleep();
    }

    public static void goSleep() throws InterruptedException {
        System.out.println("3초후 종료됩니다.");
        sleep(3000);
    }
}
