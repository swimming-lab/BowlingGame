package hello.hellospring.service;

import hello.hellospring.domain.Account;
import hello.hellospring.domain.Game;
import hello.hellospring.repository.AccountRepository;
import hello.hellospring.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class HomeService {

    private final AccountRepository accountRepository;
    private final GameRepository gameRepository;

    @Autowired
    public HomeService(AccountRepository accountRepository, GameRepository gameRepository) {
        this.accountRepository = accountRepository;
        this.gameRepository = gameRepository;
    }

    // accountName으로 account 정보 획득
    public Account getAccountThroughAccountName(String accountName) {
        Optional<Account> account = accountRepository.findByAccountName(accountName);
        if (!account.isPresent())
        {
            throw new IllegalStateException("Account not founded.");
        }

        return account.get();
    }

    // 유저의 생성 게임 갯수
    public Long getAccountGameCount(String accountName) {
        Account account = getAccountThroughAccountName(accountName);
        return gameRepository.countByAccount(account);
    }

    // 유저의 생성 게임 목록 페이징으로 불러오기
    public Page<Game> getAccountGameListPage(String accountName, int start, int length) {
        Account account = getAccountThroughAccountName(accountName);
        PageRequest pageRequest = PageRequest.of((start + 1) / length, length);

        return gameRepository.findByAccountOrderByGameIdDesc(account, pageRequest).get();
    }
}
