package kr.co.ureca.Jungjuhyeon.user.domin;

import jakarta.persistence.*;
import kr.co.ureca.Jungjuhyeon.user.domin.Enum.ReservationStatus;
import kr.co.ureca.Jungjuhyeon.seat.domain.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    private Long id;

    @Column(unique = true)
    private String nickName;

    private String name;

    private String password;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "user")
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    //== 생성 메소드 ==//
    public static User create(String nickName, String name,String password){
        return User.builder()
                .nickName(nickName)
                .name(name)
                .password(password)
                .status(ReservationStatus.NOT_RESERVED)
                .build();
    }
    //== 의자 할당 메소드 ==//
    public void allocateSeat(Seat seat){
        this.seat =seat;
        this.status = ReservationStatus.RESERVED;
    }
    //== 의자 삭제 메소드 ==//
    public void deleteSeat(Seat seat){
        this.seat = null;
        this.status = ReservationStatus.NOT_RESERVED;
    }

    //== 비밀번호 일치 확인 메소드 ==//
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
