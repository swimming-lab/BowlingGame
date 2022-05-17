package hello.hellospring.service;

import hello.hellospring.domain.Account;
import hello.hellospring.domain.Game;
import hello.hellospring.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HomeServiceTest {
    @Autowired
    HomeService homeService;

    @Test
    public void getAccountGameListPageWithoutAccountIdTest() {
        Account account = new Account();
        account.setAccountName("test");

        Page<Game> gamePage = homeService.getAccountGameListPage("test", 0, 10);

        for (Game game : gamePage) {
            System.out.println(game.getGameId());
            System.out.println(game.getTotalScore());
            System.out.println(game.getGameStatus());
            System.out.println(game.getRegTime());
            System.out.println("==============");
        }
    }

    @Test
    public void getAccountGameListPageTest() {
        Account account = new Account();
        account.setAccountName("test");

        Page<Game> gamePage = homeService.getAccountGameListPage("test", 0, 10);

        for (Game game : gamePage) {
            System.out.println(game.getGameId());
            System.out.println(game.getTotalScore());
            System.out.println(game.getGameStatus());
            System.out.println(game.getRegTime());
            System.out.println(game.getAccount().getAccountId());
            System.out.println("==============");
        }
    }
}