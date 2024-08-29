package kr.co.ureca.dto;

import lombok.Data;

@Data
public class SeatDto {

    private Long seatId;

    private Long userId;

    private Long seatNo;

    private Boolean status;
}
