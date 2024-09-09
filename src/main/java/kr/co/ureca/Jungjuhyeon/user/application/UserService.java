package kr.co.ureca.Jungjuhyeon.user.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.co.ureca.Jungjuhyeon.global.exception.BusinessException;
import kr.co.ureca.Jungjuhyeon.global.exception.errorcode.CommonErrorCode;
import kr.co.ureca.Jungjuhyeon.user.domin.User;
import kr.co.ureca.Jungjuhyeon.user.infrastructure.UserJpaRepository;
import kr.co.ureca.Jungjuhyeon.seat.presentation.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserJpaRepository userJpaRepository;
    @PersistenceContext
    EntityManager em;

     // 여기서 새로운 트랜잭션을 시작하여 forceJoin 메서드가 실행됨
     @Transactional
     public User login(SeatDto.Request.reservationDto request){
        User user = userJpaRepository.findByNickName(request.getNickName()).orElseGet(() -> forceJoin(request));

//        비밀번호가 일치한지
        if(!user.checkPassword(request.getPassword())){
            throw new BusinessException(CommonErrorCode.USER_ID_PASSWORD_FOUND);
        }
//        이미 예약한 좌석이 있는지
        if(user.getSeat() !=null){
            throw new BusinessException(CommonErrorCode.SEAT_CANNOT_BE_RESERVED);
        }

        return user;
    }

//    존재하지 않을시 생성
    public User forceJoin(SeatDto.Request.reservationDto request) {
        User newUser = User.create(request.getNickName(),request.getUserName(),request.getPassword());
        return userJpaRepository.save(newUser);
    }

}
