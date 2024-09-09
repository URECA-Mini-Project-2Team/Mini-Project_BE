package kr.co.ureca.Jungjuhyeon.seat.application;

import kr.co.ureca.Jungjuhyeon.global.exception.BusinessException;
import kr.co.ureca.Jungjuhyeon.global.exception.errorcode.CommonErrorCode;
import kr.co.ureca.Jungjuhyeon.user.domin.Enum.ReservationStatus;
import kr.co.ureca.Jungjuhyeon.seat.domain.Seat;
import kr.co.ureca.Jungjuhyeon.seat.presentation.dto.SeatDto;
import kr.co.ureca.Jungjuhyeon.user.domin.User;
import kr.co.ureca.Jungjuhyeon.seat.infrastructure.SeatJpaRepository;
import kr.co.ureca.Jungjuhyeon.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SeatService {

    private final SeatJpaRepository seatJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RedissonClient redissonClient;
//    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Transactional
    public SeatDto.Response.reservationDto reservation(Long seatNo, User user) {
        // Redis 분산 락 생성
        RLock lock = redissonClient.getLock("seat-reservation-lock-" + seatNo);

        try {
            // 락 획득 시도, 대기 시간 100ms, 락 만료 시간 1초
            boolean available = lock.tryLock(100, 2000, TimeUnit.MILLISECONDS);
            log.info("Attempting to acquire lock: " + available);


            if (!available) {
                log.info("Lock acquisition failed, seat already reserved");
                // 락을 얻지 못한 경우, 이미 예매된 것으로 처리
                throw new BusinessException(CommonErrorCode.SEAT_ALREADY_RESERVED);

            }

            // 좌석 찾기
            Seat findseat = seatJpaRepository.findBySeatNo(seatNo)
                    .orElseThrow(() -> new BusinessException(CommonErrorCode.SEAT_NOT_FOUND));

            log.info("Seat status before update: " );

            // 만약 이미 좌석이 예약되었는지 다시 한번 확인
            if (findseat.getUser() != null) {
                log.info("Seat already reserved");
                throw new BusinessException(CommonErrorCode.SEAT_ALREADY_RESERVED);
            }

            // user 영속화
            User persistentUser = userJpaRepository.findById(user.getId())
                    .orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));

            findseat.allocateUser(persistentUser);
            // 좌석을 예약 상태로 변경
            seatJpaRepository.flush();  // 명시적으로 flush

            log.info("Reservation successful");

            // 예약 정보 반환
            return SeatDto.Response.reservationDto.of(user.getId(), findseat.getSeatNo());

        } catch (InterruptedException e) {
            // 락 획득 과정에서 인터럽트가 발생한 경우 예외 처리
            throw new BusinessException(CommonErrorCode.SEAT_ALREADY_RESERVED);
        } finally {
            // 락 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("Lock released");

            }
        }
    }

    @Transactional
    public void delete(SeatDto.Request.deleteDto request) {
        User user = userJpaRepository.findByNickName(request.getNickName()).orElseThrow(()-> new BusinessException(CommonErrorCode.USER_NOT_FOUND));
        //  비밀번호가 일치한지
        if(!user.checkPassword(request.getPassword())){
            throw new BusinessException(CommonErrorCode.USER_ID_PASSWORD_FOUND);
        }
        Seat seat = user.getSeat();
        seat.deleteSeat(user);
    }



    public List<SeatDto.Response.SeatListDto> searchSeatList() {
        return seatJpaRepository.findSeatEntityGraph().stream()
                .map(seat -> {
                    User user = seat.getUser();
                    if (user != null) {
                        return SeatDto.Response.SeatListDto.of(seat.getSeatNo(), user.getStatus(), user.getNickName(), user.getName());
                    } else {
                        return SeatDto.Response.SeatListDto.of(seat.getSeatNo(), ReservationStatus.NOT_RESERVED, null, null); // 혹은 원하는 기본값으로 처리
                    }
                })
                .collect(Collectors.toList());
    }

}


