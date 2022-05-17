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

@Entity
@Table(name = "scoreBoard")
@Getter
@Setter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "scoreBoardId")
public class ScoreBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long scoreBoardId;

    // scoreBoard O- game
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gameId")
    private Game game;

    // scoreBoard -O frame
    @OneToOne(mappedBy = "scoreBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private Frame frameId = new Frame();

    @Column(nullable = false)
    private int curFrameNum;

    @Column(nullable = false)
    private int curPhaseNum;

    @Column(nullable = false)
    private int lastCalcFrameNum;


    // ScoreBoard 객체 생성자
    public static ScoreBoard createScoreBoard(Game game) {
        ScoreBoard scoreBoard = new ScoreBoard();

        scoreBoard.setGame(game);
        scoreBoard.setCurFrameNum(1);
        scoreBoard.setCurPhaseNum(1);
        scoreBoard.setLastCalcFrameNum(0);

        return scoreBoard;
    }
}
