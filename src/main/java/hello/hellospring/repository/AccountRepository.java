package hello.hellospring.repository;

import hello.hellospring.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountId(Long accountId);
    Optional<Account> findByAccountName(String accountName);
    Optional<Account> findByPassword(String password);
}
