package kr.co.ureca.exception;

import lombok.Getter;

@Getter
public enum ErrorCode implements EnumModel{

    //Conflict
    RESERVED_SEAT(40900,"C001","이미 예약된 좌석입니다. 다른 좌석을 선택해주세요."),
    RESERVED_USER(40901,"C002","이미 예약한 좌석이 존재합니다. 기존 예약을 취소해주세요.");

    private int status;
    private String code;
    private String message;
    private String detail;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    @Override
    public String getKey() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.message;
    }

}
