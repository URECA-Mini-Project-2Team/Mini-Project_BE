package kr.co.ureca.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean status = false;

    public void updateUserStatus() {
        this.status = !this.status;
    }

    public static User createUser(String name, String nickName, String password) {
        return User.builder()
                .name(name)
                .nickName(nickName)
                .password(password)
                .build();
    }


}
