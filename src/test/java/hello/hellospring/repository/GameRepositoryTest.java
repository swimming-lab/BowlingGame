package hello.hellospring.repository;

import hello.hellospring.domain.Account;
import hello.hellospring.domain.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GameRepositoryTest {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    AccountRepository accountRepository;

    /*
    @Test
    @DisplayName("유저가 생성한 게임 목록 로드")
    public void getUserGameListTest(Pageable pageable) {
        Optional<Account> testAccount = accountRepository.findByAccountName("test");
        Optional<List<Game>> gameList = gameRepository.findByAccountOrderByGameIdDesc(testAccount.get());
        for (Game game : gameList.get()) {
            System.out.println(game.toString());
        }
    }
    */

    @Test
    @DisplayName("유저 게임 목록 페이징 로드")
    public void checkUserGameList() {
        Account account = new Account();
        account.setAccountId(1L);
        Optional<Page<Game>> gamePage = gameRepository.findByAccountOrderByGameIdDesc(account, Pageable.ofSize(10));

        for (Game game : gamePage.get()) {
            System.out.println(game.getGameId() + " : " + game.getTotalScore());
        }
    }
}