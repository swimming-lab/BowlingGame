package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hello.hellospring.constant.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="game")
@Getter
@Setter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gameId")
public class Game extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    // game >- account
    @JsonIgnore // JSON 객체 생성 시 account 엔티티 순환 참조를 막기 위한 어노테이션
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountId")
    private Account account;

    // game -O scoreBoard
    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private ScoreBoard scoreBoardId = new ScoreBoard();

    @Enumerated(EnumType.STRING)
    private Status gameStatus;

    @Column
    private int totalScore;

    public static Game createGame(Account account) {
        Game game = new Game();

        game.setAccount(account);
        game.setGameStatus(Status.PROCESS);
        game.setTotalScore(0);

        return game;
    }
}