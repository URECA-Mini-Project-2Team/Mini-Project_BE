package kr.co.ureca.Jungjuhyeon.seat.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.ureca.Jungjuhyeon.user.domin.Enum.ReservationStatus;
import lombok.Builder;
import lombok.Getter;

public class SeatDto {

    public static class Request {
        @Getter
        @Builder
        public static class reservationDto {
            @NotBlank
            private String userName;
            @NotBlank
            private String nickName;
            @NotBlank
            private String password;
            @NotNull
            private Long seatNo;

            public static Request.reservationDto of(String userName,String nickName,String password,Long seatNo) {
                return reservationDto.builder()
                        .userName(userName)
                        .nickName(nickName)
                        .password(password)
                        .seatNo(seatNo)
                        .build();
            }
        }

        @Getter
        @Builder
        public static class deleteDto {
            @NotBlank
            private String nickName;
            @NotBlank
            private String password;

            public static Request.deleteDto of(String nickName, String password){
                return deleteDto.builder()
                        .nickName(nickName)
                        .password(password)
                        .build();
            }

        }
    }
    public static class Response {
        @Builder
        @Getter
        public static class reservationDto {
            private Long userId;
            private Long seatNo;

            public static reservationDto of(Long userId, Long seatId) {
                return reservationDto.builder()
                        .userId(userId)
                        .seatNo(seatId)
                        .build();
            }
        }
        @Builder
        @Getter
        public static class SeatListDto {
            private Long seatNo;
            private ReservationStatus status;
            private String nickName;
            private String userName;

            public static SeatListDto of(Long seatNo,ReservationStatus status,String nickName,String userName){
                return SeatListDto.builder()
                        .seatNo(seatNo)
                        .status(status)
                        .nickName(nickName)
                        .userName(userName)
                        .build();
            }

        }

    }

}
