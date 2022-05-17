package hello.hellospring.repository;

import hello.hellospring.domain.Account;
import hello.hellospring.domain.Game;
import hello.hellospring.domain.ScoreBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreBoardRepository extends JpaRepository<ScoreBoard, Long> {
    Optional<ScoreBoard> findByGame(Game game);
}
