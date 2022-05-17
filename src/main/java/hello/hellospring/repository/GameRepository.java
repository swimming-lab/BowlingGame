package hello.hellospring.repository;

import hello.hellospring.domain.Account;
import hello.hellospring.domain.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Page<Game>> findByAccountOrderByGameIdDesc(Account account, Pageable pageable);
    Long countByAccount(Account account);
}
