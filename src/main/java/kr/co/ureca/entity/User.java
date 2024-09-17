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

    private Boolean status;

    @OneToOne(mappedBy = "user")
    private Seat seat;

    public User(String userName, String password, String nickName){
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
    }

    public User() {

    }

    public User(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public void updateUser(String userName, String password, String nickName, Boolean status){
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
        this.status = status;
    }

    public void assignSeat(Seat seat){
        this.seat = seat;
        this.status = true;
    }
    public void removeSeat(){
        this.seat = null;
        this.status = false;
    }
}
