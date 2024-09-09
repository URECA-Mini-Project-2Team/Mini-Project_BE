package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userdb")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickName;

    @Column(name = "password", nullable = false)
    private String password;

}
