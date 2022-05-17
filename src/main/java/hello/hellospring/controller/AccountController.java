package hello.hellospring.controller;

import hello.hellospring.domain.Account;
import hello.hellospring.dto.AccountFormDto;
import hello.hellospring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController
{
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Autowired  // Autowired로 Controller 등록 시 자동으로 Service도 등록, 의존성 주입
    public AccountController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/account/new")
    public String createAccountForm(Model model)
    {
        model.addAttribute("accountFormDto", new AccountFormDto());
        return "account/accountForm_2";
    }

    @PostMapping("/account/new")
    public String createAccount(AccountFormDto accountFormDto)
    {
        Account account = Account.createAccount(accountFormDto, passwordEncoder);
        accountService.signUp(account);

        return "redirect:/";
    }

    @GetMapping("/account/login")
    public String createLoginForm()
    {
        return "account/loginForm_2";
    }

    @GetMapping("/account/login/error")
    public String loginError(Model model)
    {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "account/loginForm_2";
    }

    @GetMapping("/account/login/success")
    public String loginSuccess() {
        return "redirect:/home";
    }
}

