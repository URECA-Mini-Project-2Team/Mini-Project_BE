package kr.co.ureca.Jungjuhyeon.global.exception;

import kr.co.ureca.Jungjuhyeon.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;
}
