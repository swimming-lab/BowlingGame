package hello.hellospring.service;

import hello.hellospring.domain.Account;
import hello.hellospring.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional  // EntityManager의 persist를 유지하기 위해 선언
public class AccountService implements UserDetailsService
{
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository)
        {
            this.accountRepository = accountRepository;
        }

    // accountName 중복 검증 후 데이터 저장
    public Account signUp(Account account)
    {
        validateDuplicateAccount(account);
        return accountRepository.save(account);
    }

    private void validateDuplicateAccount(Account account)
    {
        Optional<Account> findAccount = accountRepository.findByAccountName(account.getAccountName());

        if (findAccount.isPresent())
        {
            throw new IllegalStateException("Already signed account name.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException
    {
        Optional<Account> findAccount = accountRepository.findByAccountName(accountName);

        // accountName 값의 계정 데이터가 없는 경우
        if (!findAccount.isPresent())
        {
            throw new UsernameNotFoundException(accountName);
        }

        // Optional 타입 -> Account 클래스 타입
        Account account = findAccount.get();
        return User.builder()
                .username(account.getAccountName())
                .password(account.getPassword())
                .roles(account.getAccountRole().toString()) // Enum타입 -> String 타입
                .build();
    }
}
