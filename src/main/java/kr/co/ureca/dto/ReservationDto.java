package kr.co.ureca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationDto {

    private String userName;

    private String nickName;

    private String password;

    private Long seatNo;

}
