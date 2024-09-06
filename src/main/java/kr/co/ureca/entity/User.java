package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "user")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;

    @JoinColumn(name = "seatid")
    @OneToOne
    private Seat seat;

    @Column(name = "username", unique = true)
    private String userName;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "password")
    private String password;

    @Column(name = "hasreservation")
    @ColumnDefault("false")
    private Boolean hasReservation;

    public void updateUserReservation(Seat seat, Boolean hasReservation) {
        this.seat = seat;
        this.hasReservation = hasReservation;
    }
}
