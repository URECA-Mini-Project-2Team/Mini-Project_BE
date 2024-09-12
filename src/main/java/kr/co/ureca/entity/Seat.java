package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seatdb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seatid")
    private Long seatId;

    @Column(name = "seatno", nullable = false)
    private Long seatNo;

    @JoinColumn(name = "userid")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public void updateUser(User user) {
        this.user = user;
    }

}
