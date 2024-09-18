package kr.co.ureca.service;

import kr.co.ureca.dto.CustomeUserDetails;
import kr.co.ureca.dto.ReservationDeleteDto;
import kr.co.ureca.dto.ReservationRequestDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReservationService {


    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final PasswordEncoder passwordEncoder;

    private final ReentrantLock lock = new ReentrantLock();

    public List<SeatDto> getAllSeats() {
        List<Seat> seats = seatRepository.findAll();

        return seats.stream().map(seat -> {
            SeatDto dto = new SeatDto();
            dto.setSeatNo(seat.getSeatNo());
            dto.setStatus(false);
            if(seat.getUser() != null){
                dto.setNickName(seat.getUser().getNickName());
                dto.setUserName(seat.getUser().getUserName());
                dto.setStatus(true);
            }
            return dto;
        }).collect(Collectors.toList());
    }
    public boolean selectSeatToUser(Long seatNo) {
        System.out.println("Attempting to reserve seat with ID: " + seatNo);
        lock.lock();
        try {
            Seat seat = seatRepository.findById(seatNo)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (seat.getUser() != null && seat.getUser().getStatus()) {
                System.out.println("User already has a reserved seat.");
                throw new RuntimeException("이미 예약된 좌석이 있습니다. 취소 후 이용해주세요.");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            User user = userRepository.findByUserName(userName);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + userName);
            }

            user.assignSeat(seat);
            seat.updateSeat(user);

            userRepository.save(user);
            seatRepository.save(seat);

            System.out.println("Seat reservation successful for user: " + userName);
            return true;
        } catch (Exception e) {
            System.out.println("Error during seat reservation: " + e.getMessage());
            return false;
        } finally {
            lock.unlock();
        }
    }
    public boolean deleteSeat(ReservationDeleteDto reservationDeleteDto) throws Exception {
        Optional<User> userOpt = userRepository.findByNickName(reservationDeleteDto.getNickName());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 유저가 아닙니다.");
        }

        User user = userOpt.get();
        System.out.println(user);
        // 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(reservationDeleteDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호를 확인해주세요.");
        }
        if (!user.getStatus()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "예약된 좌석이 없습니다.");
        }

        Seat seat = seatRepository.findById(user.getSeat().getId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        seat.removeUser();
        seatRepository.save(seat);

        user.removeSeat();
        userRepository.save(user);

        return true;
    }
}
