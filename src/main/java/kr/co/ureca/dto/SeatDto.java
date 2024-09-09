package kr.co.ureca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatDto {

    private Long seatNo;

    private Boolean status;

    private String nickName;

    private String userName;

}
