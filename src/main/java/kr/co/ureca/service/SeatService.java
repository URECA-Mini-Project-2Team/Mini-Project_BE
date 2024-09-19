package kr.co.ureca.service;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public List<SeatDto.ResponseDto.SeatListDto> getSeatList() {
        List<Seat> seats = seatRepository.findAll();
        return seats.stream()
                .map(seat -> {
                        User user = seat.getUser();
                        if (user == null) {
                            return SeatDto.ResponseDto.SeatListDto.of(seat.getSeatNo(), false, "", "");
                        } else {
                            return SeatDto.ResponseDto.SeatListDto.of(seat.getSeatNo(), true, user.getNickName(), user.getName());
                        }
                }).collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Seat reservationSeat(Seat seat, User user) throws Exception {
        if (user.getStatus()) throw new Exception("이미 등록된 자리가 있습니다.");
        seat.updateUser(user);
        seatRepository.save(seat);
        return seat;
    }

    public Seat deleteSeat() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Optional<User> userOpt = userRepository.findByName(userName);
        if(userOpt.isEmpty()){
            throw new Exception("등록되지 않은 사용자입니다.");
        }
        User user = userOpt.get();
        if(!user.getStatus()){
            throw new Exception("예약된 좌석이 없습니다.");
        }

        Optional<Seat> seatOptional = seatRepository.findOpByUser(user);

        if (seatOptional.isPresent()) {
            Seat seat = seatOptional.get();
            seat.updateUser(null);
            seatRepository.save(seat);

            userService.updateUserStatus(user);

            return seat;
        }
        else throw new Exception("등록된 자리가 없습니다.");
    }
//    public Seat deleteSeat(User user) throws Exception {
//        Optional<Seat> seatOptional = seatRepository.findOpByUser(user);
//
//        if (seatOptional.isPresent()) {
//            Seat seat = seatOptional.get();
//            seat.updateUser(null);
//            seatRepository.save(seat);
//
//            return seat;
//        }
//        else throw new Exception("등록된 자리가 없습니다.");
//    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Seat checkSeatExistsOrNot(Long seatNo) throws Exception {
        Optional<Seat> seatOptional = seatRepository.findOpBySeatNo(seatNo);
        if (seatOptional.isEmpty()) throw new Exception("존재하지 않은 자리입니다.");
        Seat seat = seatOptional.get();
        if (seat.getUser() != null) throw new Exception("이미 예약된 좌석입니다.");

        return seat;
    }
}
