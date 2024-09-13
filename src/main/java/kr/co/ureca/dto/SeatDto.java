package kr.co.ureca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class SeatDto {

    public static class ResponseDto {

        @Getter
        @Builder
        public static class SeatListDto {

            @NotBlank
            private Long seatNo;

            @NotBlank
            private Boolean status;

            @NotNull
            private String nickName;

            @NotNull
            private String userName;

            public static SeatListDto of(Long seatNo, Boolean status, String nickName, String userName) {
                return SeatListDto.builder()
                        .seatNo(seatNo)
                        .status(status)
                        .nickName(nickName)
                        .userName(userName)
                        .build();
            }
        }
    }

    public static class RequestDto {

        @Getter
        @Builder
        public static class ReservationDto {

            @NotBlank
            private String userName;

            @NotBlank
            private String nickName;

            @NotBlank
            private String password;

            @NotBlank
            private Long seatNo;

        }

        @Getter
        public static class DeleteDto {

            @NotBlank
            private String nickName;

            @NotBlank
            private String password;

        }
    }
}
