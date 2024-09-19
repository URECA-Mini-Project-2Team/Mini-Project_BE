package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = null;
        if (authentication != null) {
            userName = authentication.getName();
            System.out.println("=================");
            System.out.println("Authenticated username: " + userName);
            System.out.println("=================");
        } else {
            System.out.println("Authentication is null");
        }

        User user = userService.checkUserExistOrNot(userName);
        seatService.reservationSeat(seat, user);
        userService.updateUserStatus(user);
    }

    public static void goSleep() throws InterruptedException {
        System.out.println("3초후 종료됩니다.");
        sleep(3000);
    }
}
