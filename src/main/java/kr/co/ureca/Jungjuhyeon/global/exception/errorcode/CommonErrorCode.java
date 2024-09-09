package kr.co.ureca.Jungjuhyeon.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    // 공용 처리
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "4000", "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "4040", "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5000", "알수없는 에러 관리자에게 문의"),


    //좌석 4100
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "4101", "해당 좌석을 찾을 수 없습니다."),
    SEAT_ALREADY_RESERVED(HttpStatus.CONFLICT, "4102", "해당 좌석은 이미 예약되었습니다."),
    SEAT_CANNOT_BE_RESERVED(HttpStatus.CONFLICT, "4103", "이미 예약한 좌석이 있습니다."),

    //유저 4200
    USER_ID_PASSWORD_FOUND(HttpStatus.BAD_REQUEST, "4201", "아이디 또는 비밀번호가 잘못되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "4202", "해당하는 유저가 없습니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
