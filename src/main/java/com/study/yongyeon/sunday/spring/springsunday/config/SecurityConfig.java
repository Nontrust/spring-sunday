package com.study.yongyeon.sunday.spring.springsunday.config;

import com.study.yongyeon.sunday.spring.springsunday.dto.ClubLoginSuccessHandler;
import com.study.yongyeon.sunday.spring.springsunday.security.service.ClubUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
//    private final ClubUserDetailService clubUserDetailService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .authorizeRequests(auth->
                    auth    .antMatchers("/permit/all").permitAll()
                            .antMatchers("/permit/member").hasRole("USER")
                            .antMatchers("/permit/admin").hasRole("ADMIN"))
                .formLogin()
//                .userDetailsService(clubUserDetailService)
                .and().logout()
                .and().oauth2Login()
                .successHandler(clubMemberSuccessHandler());
//                .and()
//                .rememberMe()
//                .tokenValiditySeconds(60*60*24*7);

        return http.build();
    }

    @Bean
    public ClubLoginSuccessHandler clubMemberSuccessHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }
}
