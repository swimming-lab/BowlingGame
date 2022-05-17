
package hello.hellospring.config;

import hello.hellospring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // 로그인
        http.formLogin()
                .loginPage("/account/login")
                .loginProcessingUrl("/account/login")
                .usernameParameter("accountName")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .failureUrl("/account/login/error")
                .permitAll();

        // 로그아웃
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/account/logout"))
                .logoutSuccessUrl("/account/login")
                .invalidateHttpSession(true);

        // 페이지 권한
        http.authorizeRequests()
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/account/**").permitAll()
                .mvcMatchers("/assets/**").permitAll()
                .anyRequest().authenticated();

        http.sessionManagement()
                .invalidSessionUrl("/account/login");

        // 미인증 유저 접근 핸들러
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    // Spring Security 인증 처리
    // AuthenticationManagerBuilder가 생성한 AuthenticationManager를 통해 이뤄짐
    // userDetailService를 구현하는 객체로 accountService 지정
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder());
    }

    // 로그인 없이 접근 가능해야 하는 resource 예외처리
    @Override
    public void configure(WebSecurity web) throws Exception
    {

        web.ignoring().antMatchers("/js/**", "/css/**", "/img/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
