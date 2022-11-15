package com.example.javasecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.javasecurity.config.oauth.PrincipalOauth2UserService;

@Configuration // IoC 등록(메모리에 띄워줌)
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // password를 암호화할 때 사용하는 인코더
    @Bean // 해당 메서드의 리턴되는 오브젝트를 IoC 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // csrf 토큰 비활성화(테스트시 걸어두는게 좋음)
            .authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // admin으로 시작하는 주소는 admin만 접근 가능
                .antMatchers("/user/**").authenticated() // user로 시작하는 주소는 user만 접근 가능
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER')") // manager로 시작하는 주소는 manager만 접근 가능
                .anyRequest().permitAll() // 그 외 나머지 주소는 누구나 접근 가능
                .and()
            .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

    }
}
