package hello.hellospring.service;

import hello.hellospring.constant.Status;
import hello.hellospring.domain.Account;
import hello.hellospring.domain.Frame;
import hello.hellospring.domain.Game;
import hello.hellospring.domain.ScoreBoard;
import hello.hellospring.dto.GameFormDto;
import hello.hellospring.repository.AccountRepository;
import hello.hellospring.repository.FrameRepository;
import hello.hellospring.repository.GameRepository;
import hello.hellospring.repository.ScoreBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GameService {
    private final AccountRepository accountRepository;
    private final GameRepository gameRepository;
    private final ScoreBoardRepository scoreBoardRepository;
    private final FrameRepository frameRepository;

    @Autowired
    public GameService(AccountRepository accountRepository, GameRepository gameRepository, ScoreBoardRepository scoreBoardRepository, FrameRepository frameRepository) {
        this.accountRepository = accountRepository;
        this.gameRepository = gameRepository;
        this.scoreBoardRepository = scoreBoardRepository;
        this.frameRepository = frameRepository;
    }

    public Game startGame(String accountName) {
        Account account = accountRepository.findByAccountName(accountName).get();

        Game game = Game.createGame(account);
        ScoreBoard scoreBoard = ScoreBoard.createScoreBoard(game);
        Frame frame = Frame.createFrame(scoreBoard);

        scoreBoard.setFrameId(frame);
        game.setScoreBoardId(scoreBoard);

        return gameRepository.save(game);
    }

    public Game saveGame(GameFormDto gameFormDto) {

        Game game = gameRepository.findById(gameFormDto.getGameId()).get();
        ScoreBoard scoreBoard = scoreBoardRepository.findByGame(game).get();
        Frame frame = frameRepository.findByScoreBoard(scoreBoard).get();

        scoreBoard.setFrameId(frame);
        game.setScoreBoardId(scoreBoard);

        frame.setScoreData(gameFormDto.getScoreData());
        scoreBoard.setCurFrameNum(gameFormDto.getCurFrameNum());
        scoreBoard.setCurPhaseNum(gameFormDto.getCurPhaseNum());
        scoreBoard.setLastCalcFrameNum(gameFormDto.getLastCalcFrameNum());
        game.setTotalScore(gameFormDto.getTotalScore());

        // 모든 프레임 점수 계산이 완료되었을 때 상태 값 FINISH로 변경
        if (gameFormDto.getLastCalcFrameNum() > 10) {
            game.setGameStatus(Status.FINISH);
        }

        return gameRepository.save(game);
    }

    // 목록의 게임 선택 후 게임 데이터를 불러오는 메소드
    public GameFormDto getGameResult(Long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (!game.isPresent())
        {
            throw new IllegalStateException("Game not founded.");
        }

        ScoreBoard scoreBoard = scoreBoardRepository.findByGame(game.get()).get();
        Frame frame = frameRepository.findByScoreBoard(scoreBoard).get();

        String scoreData = frame.getScoreData();
        int curFrameNum = scoreBoard.getCurFrameNum();
        int curPhaseNum = scoreBoard.getCurPhaseNum();
        int lastCalcFrameNum = scoreBoard.getLastCalcFrameNum();
        int totalScore = game.get().getTotalScore();

        GameFormDto gameFormDto = new GameFormDto();
        gameFormDto.setGameId(gameId);
        gameFormDto.setLastCalcFrameNum(lastCalcFrameNum);
        gameFormDto.setCurFrameNum(curFrameNum);
        gameFormDto.setCurPhaseNum(curPhaseNum);
        gameFormDto.setScoreData(scoreData);
        gameFormDto.setTotalScore(totalScore);

        return gameFormDto;
    }
}
