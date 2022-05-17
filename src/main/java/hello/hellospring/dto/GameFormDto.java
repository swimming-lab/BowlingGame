package hello.hellospring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameFormDto {
    // private List<List<Integer>> score;
    // private List<Integer> score10Frame;
    private Long gameId;
    private String scoreData;
    private int curFrameNum;
    private int curPhaseNum;
    private int lastCalcFrameNum;
    private int totalScore;
}
