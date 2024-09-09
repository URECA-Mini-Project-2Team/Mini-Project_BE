package kr.co.ureca.Jungjuhyeon.seat.presentation;

import jakarta.validation.Valid;
import kr.co.ureca.Jungjuhyeon.global.response.SuccessResponse;
import kr.co.ureca.Jungjuhyeon.seat.application.SeatService;
import kr.co.ureca.Jungjuhyeon.user.application.UserService;
import kr.co.ureca.Jungjuhyeon.user.domin.User;
import kr.co.ureca.Jungjuhyeon.seat.presentation.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ureca")
public class SeatController {

    private final SeatService seatService;
    private final UserService userService;

    @PatchMapping("/reservation")
    public SuccessResponse<SeatDto.Response.reservationDto> reservation(@RequestBody @Valid SeatDto.Request.reservationDto reuqest){
        User user = userService.login(reuqest);
        SeatDto.Response.reservationDto response = seatService.reservation(reuqest.getSeatNo(),user);
        return SuccessResponse.success(response);
    }

    @PatchMapping("/delete")
    public SuccessResponse<String> delete(@RequestBody @Valid SeatDto.Request.deleteDto request){
        seatService.delete(request);
        return SuccessResponse.successWithoutResult("성공");
    }

    @GetMapping()
    private SuccessResponse<List<SeatDto.Response.SeatListDto>> searchSeatList(){
        List<SeatDto.Response.SeatListDto> response = seatService.searchSeatList();
        return  SuccessResponse.success(response);
    }
}
