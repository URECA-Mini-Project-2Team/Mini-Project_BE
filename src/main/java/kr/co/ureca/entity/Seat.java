package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long seatNo;

    private boolean status;

    @OneToOne(mappedBy = "seat")
    private User user;
}
