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

    //연관관계 주인 Seat
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Seat(Long seatNo){
        this.seatNo = seatNo;
    }

    public Seat() {

    }

    public void updateSeat(User user){
        this.user = user;
        if(user != null){
            user.assignSeat(this);
        }
    }
    public void removeUser(){
        if(user != null) {
            user.removeSeat();
            this.user = null;
        }
    }
}
