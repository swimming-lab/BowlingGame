package hello.hellospring.repository;

import hello.hellospring.domain.Frame;
import hello.hellospring.domain.ScoreBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FrameRepository extends JpaRepository<Frame, Long> {
    Optional<Frame> findByScoreBoard(ScoreBoard scoreBoard);
}
