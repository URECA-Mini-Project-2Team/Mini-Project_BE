package kr.co.ureca.Entity;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(mappedBy = "user")
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private String userName;
    private String email;
}
