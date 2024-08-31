package kr.co.ureca.controller;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.dto.UserDto;
import kr.co.ureca.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping()
    public ResponseEntity<List<SeatDto>> getSeatList() {
        List<SeatDto> SetList = seatService.getSeatList();
        return ResponseEntity.ok(SetList);
    }

    @PatchMapping("/delete")
    public ResponseEntity deleteSeat(@RequestBody UserDto userDto) throws Exception {
        return seatService.deleteSeat(userDto);
    }
}
