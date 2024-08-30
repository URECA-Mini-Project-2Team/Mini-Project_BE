package kr.co.ureca.service;

import kr.co.ureca.dto.ReservationRequest;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.entity.User;
import kr.co.ureca.exception.CustomException;
import kr.co.ureca.exception.ErrorCode;
import kr.co.ureca.repository.SeatRepository;
import kr.co.ureca.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    UserRepository mockUserRepository;

    @Mock
    private SeatRepository mockSeatRepository;

    @Autowired
    private SeatRepository seatRepository;

    @InjectMocks
    ReservationService reservationService;

    private ReservationRequest request;

    @BeforeEach
    void setUp() {
        request = new ReservationRequest("userName", "1111", "userNickName", 1L);
    }

    @Test
    void initSeats() {
        //given
        //when
        long count = seatRepository.count();
        //then
        assertThat(count).isEqualTo(30);

    }

    @Test
    @DisplayName("예약 성공")
    void reserve() {
        //given

        Seat seat = Seat.builder().seatNo(1L).status(false).build();  // 예약 가능한 좌석
        User user = User.builder().nickName("userNickName").userName("userName").password("password").build();

        when(mockSeatRepository.findBySeatNo(1L)).thenReturn(Optional.of(seat));
        when(mockUserRepository.findByNickName("userNickName")).thenReturn(Optional.of(user));

        //when
        Seat reservedSeat = reservationService.reserve(request);

        //then
        assertThat(reservedSeat.getStatus()).isTrue();
        assertThat(reservedSeat.getUser().getNickName()).isEqualTo("userNickName");
        verify(mockUserRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이미 예약된 좌석은 예약 불가")
    void ifReservedSeatThrowException() {
        //given
        Seat reservedSeat = Seat.builder().seatNo(1L).status(true).build();  // 이미 예약된 좌석
        when(mockSeatRepository.findBySeatNo(1L)).thenReturn(Optional.of(reservedSeat));

        //when
        CustomException exception = assertThrows(CustomException.class, () -> reservationService.reserve(request));

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVED_SEAT);
        verify(mockUserRepository, never()).save(any(User.class));
        verify(mockSeatRepository, never()).save(any(Seat.class));
    }

    @Test
    @DisplayName("이미 예약한 사용자는 예약 불가")
    void ifReservedUserThrowException() {
        //given
        Seat seat = Seat.builder().seatNo(1L).status(false).build();  // 예약 가능한 좌석
        User reservedUser = User.builder().nickName("userNickName").userName("userName").password("1111").hasReservation(true).build();  // 이미 예약한 사용자

        when(mockSeatRepository.findBySeatNo(1L)).thenReturn(Optional.of(seat));
        when(mockUserRepository.findByNickName("userNickName")).thenReturn(Optional.of(reservedUser));

        //when //then
        CustomException exception = assertThrows(CustomException.class, () -> reservationService.reserve(request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVED_USER);
        verify(mockUserRepository, never()).save(any(User.class));
        verify(mockSeatRepository, never()).save(any(Seat.class));
    }
}