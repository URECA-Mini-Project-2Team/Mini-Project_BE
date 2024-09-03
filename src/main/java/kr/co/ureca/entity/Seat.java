package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seat")
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seatid")
    private Long seatId;

    @JoinColumn(name = "userid")
    @OneToOne
    private User user;

    @Column(name = "seatno")
    private Long seatNo;

    @Column(name = "status")
    private Boolean status;
}
