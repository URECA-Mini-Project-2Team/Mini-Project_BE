package kr.co.ureca.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickName;

    private boolean hasReservation;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

}
