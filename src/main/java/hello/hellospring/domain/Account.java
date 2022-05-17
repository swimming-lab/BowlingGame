package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hello.hellospring.constant.Role;
import hello.hellospring.dto.AccountFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "accountId")
public class Account
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long accountId;

    @Column(length = 255, nullable = false, unique = true)
    private String accountName;

    @Column(length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role accountRole;

    // account -< game
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Game> games = new ArrayList<>();

    // Account 객체 생성
    public static Account createAccount(AccountFormDto accountFormDto, PasswordEncoder passwordEncoder)
    {
        Account account = new Account();
        account.setAccountName(accountFormDto.getAccountName());
        // 비밀번호 값 변환 후 저장
        String password = passwordEncoder.encode(accountFormDto.getPassword());
        account.setPassword(password);
        // USER 권한으로 계정 생성
        account.setAccountRole(Role.USER);

        return account;
    }
}
