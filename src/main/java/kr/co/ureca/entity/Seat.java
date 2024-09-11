package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @Column(nullable = false)
    private Long seatNo;

    private Boolean status;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updateSeat(Long seatNo, Boolean status){
        this.seatNo = seatNo;
        this.status = status;
    }

    public void assignUser(User user){
        this.user = user;
    }
}
