package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="frame")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "frameId")
public class Frame {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long frameId;

    // frame O- scoreBoard
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="scoreBoardId")
    private ScoreBoard scoreBoard;

    @Column
    private String scoreData;

    public static Frame createFrame(ScoreBoard scoreBoard) {
        Frame frame = new Frame();
        frame.setScoreBoard(scoreBoard);
        return frame;
    }
}
