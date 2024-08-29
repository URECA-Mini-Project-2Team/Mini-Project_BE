package kr.co.ureca.controller;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
