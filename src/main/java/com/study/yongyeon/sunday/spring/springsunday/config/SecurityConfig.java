package com.study.yongyeon.sunday.spring.springsunday.config;

import com.study.yongyeon.sunday.spring.springsunday.security.service.ClubLoginFailureHandler;
import com.study.yongyeon.sunday.spring.springsunday.security.service.ClubLoginSuccessHandler;
import com.study.yongyeon.sunday.spring.springsunday.security.service.ClubUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final ClubUserDetailService userDetailsService;
//    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

//                http.authorizeRequests(auth->
//                    auth    .antMatchers("/permit/all").permitAll()
//                            .antMatchers("/permit/member").hasRole("USER")
//                            .antMatchers("/permit/admin").hasRole("ADMIN"));

//
            http.formLogin();
            http.csrf().disable();
//                .failureHandler(authenticationFailureHandler())
            http.logout();
            http.oauth2Login().successHandler(successHandler());

            http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);

//        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

//        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(apiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler(){
//        return new ClubLoginFailureHandler();
//    }
}
