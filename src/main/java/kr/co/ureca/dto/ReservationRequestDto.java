package kr.co.ureca.dto;

import lombok.Data;

@Data
public class ReservationRequestDto {

    private String userName;
    private String nickName;
    private String password;
    private Long seatNo;
}
