package kr.co.ureca.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String userName;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickName;

    private Boolean hasReservation;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    public void updateUser(String userName, String password, String nickName, Boolean hasReservation, Seat seat){
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
        this.hasReservation = hasReservation;
        this.seat = seat;
    }

    public void assignSeat(Seat seat){
        this.seat = seat;
        this.hasReservation = true;
    }
}
