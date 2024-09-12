package kr.co.ureca.controller;

import kr.co.ureca.dto.ReservationDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.dto.UserDto;
import kr.co.ureca.entity.User;
import kr.co.ureca.service.SeatService;
import kr.co.ureca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class SeatController {

    private final SeatService seatService;
    private final UserService userService;

    @Autowired
    public SeatController(SeatService seatService, UserService userService) {
        this.seatService = seatService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<SeatDto>> getSeatList() {
        List<SeatDto> SetList = seatService.getSeatList();
        return ResponseEntity.ok(SetList);
    }

    @PatchMapping("/reservation")
    public ResponseEntity<Void> reservationSeat(@RequestBody ReservationDto reservationDto) throws Exception {
        User user = userService.checkExistOrNot(reservationDto);
        seatService.reservationSeat(reservationDto.getSeatNo(), user);
        userService.updateUserStatus(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/delete")
    public ResponseEntity<Void> deleteSeat(@RequestBody UserDto userDto) throws Exception {
        User user = userService.checkExistOrNot(userDto);
        seatService.deleteSeat(user);
        userService.updateUserStatus(user);
        return ResponseEntity.ok().build();
    }
}
