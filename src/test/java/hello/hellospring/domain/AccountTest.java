package hello.hellospring.domain;

import hello.hellospring.constant.Role;
import hello.hellospring.constant.Status;
import hello.hellospring.repository.AccountRepository;
import hello.hellospring.repository.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountTest {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    GameRepository gameRepository;

    @PersistenceContext
    EntityManager em;

    public Game createGame() {
        Game game = new Game();
        game.setGameStatus(Status.PROCESS);
        game.setTotalScore(0);

        return game;
    }

    @Test
    public void cascadeTest() {
        Account account = new Account();
        account.setAccountName("cascade");
        account.setPassword("sdfsdfsd");
        account.setAccountRole(Role.USER);
        account.setAccountId(Long.valueOf(11));

        em.persist(account);

        for (int i = 0; i < 3; i++)
        {
            Game game = this.createGame();
            game.setAccount(account);
            em.persist(game);
        }

        em.clear();

        Account savedAccount = accountRepository.findById(account.getAccountId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedAccount.getGames().size());
    }
}
