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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GameTest {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    GameRepository gameRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("게임 인스턴스 생성")
    @WithMockUser(username = "createtest", roles = "USER")
    public void createGameTest() {
        Account account = new Account();
        account.setAccountName("createtest");
        account.setPassword("sdfsdfsd");
        account.setAccountRole(Role.USER);
        account.setAccountId(Long.valueOf(11));

        accountRepository.save(account);
        //em.flush();

        Game game = new Game();
        game.setAccount(account);
        game.setGameStatus(Status.PROCESS);
        game.setTotalScore(0);

        Game savedGame = gameRepository.save(game);
        System.out.println(savedGame.toString());
    }

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "auditor", roles = "USER")
    public void auditingTest() {
        Game newGame = new Game();
        gameRepository.save(newGame);

        em.flush();
        em.clear();

        Game game = gameRepository.findById(newGame.getGameId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("register time : " + game.getRegTime());
        System.out.println("update time : " + game.getUpdateTime());
        System.out.println("create time : " + game.getCreatedBy());
        System.out.println("modify time : " + game.getModifiedBy());
    }
}