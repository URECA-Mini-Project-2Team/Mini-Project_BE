package kr.co.ureca.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seatid")
    private Long seatId;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "seatno")
    private Long seatNo;

    @Column(name = "status")
    private boolean status;
}
