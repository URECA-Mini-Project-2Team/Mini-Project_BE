package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seatid")
    private Long seatId;

    @JoinColumn(name = "userid")
    @OneToOne
    private User user;

    @Column(name = "seatno", nullable = false)
    private Long seatNo;

    @Column(name = "status")
    @ColumnDefault("false")
    private Boolean status;

    public void updateSeatReservation(User user, Boolean status) {
        this.user = user;
        this.status = status;
    }
}
