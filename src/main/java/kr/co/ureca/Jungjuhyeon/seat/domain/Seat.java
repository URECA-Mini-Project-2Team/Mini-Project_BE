package kr.co.ureca.Jungjuhyeon.seat.domain;


import jakarta.persistence.*;
import kr.co.ureca.Jungjuhyeon.user.domin.Enum.ReservationStatus;
import kr.co.ureca.Jungjuhyeon.user.domin.User;
import lombok.*;
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="seat_id")
    private Long id;

    private Long seatNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //== 자리 할당 메소드 ==//
    public void allocateUser(User user) {
        this.user = user;
        user.allocateSeat(this); // 양방향 관계 설정
    }

    //== 의자 삭제 메소드 ==//
    public void deleteSeat(User user){
        this.user = null;
        user.deleteSeat(this);
    }

}
