package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @Column(nullable = false, unique = true)
    private Long seatNo;

    @JoinColumn(name = "userId")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    public Seat(Long seatNo){
        this.seatNo = seatNo;
    }

    public void updateUser(User user) {
        this.user = user;
    }

}
