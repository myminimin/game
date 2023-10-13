package com.no3.game.config;

import com.no3.game.oauth.PrincipalOauth2UserService;
import com.no3.game.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")    // 로그인 페이지 URL 설정
                .defaultSuccessUrl("/")         // 로그인 성공 시 이동할 URL 설정
                .usernameParameter("email")     // 로그인 시 사용할 파라미터 이름으로 email을 지정
                .failureUrl("/members/login/error") // 로그인 실패 시 이동할 URL 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 URL
                .logoutSuccessUrl("/")          // 로그아웃 성공 시 이동할 URL 설정
        ;

        http.csrf().disable();

        http.authorizeRequests()//메소드는 URL 패턴에 따른 접근 권한을 설정
                .mvcMatchers("/css/**","/js/**","/img/**", "/fonts/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**","/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                // '/admin/**' 패턴은 "ADMIN" 역할을 가진 사용자에게만 허용
                .mvcMatchers("/review/list", "/review/read/**").permitAll()
                // '/review/list", "/review/read/**" 패턴은 누구나 볼 수 있게 허용
                .anyRequest().authenticated();

        http.exceptionHandling() //인증 실패 시 사용자 정의 인증 진입 지점(CustomAuthenticationEntryPoint)을 설정
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
        ;

        return http.build(); //SecurityFilterChain을 생성하고 반환
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } // 해시 함수를 이용해 비밀번호를 암호화하여 저장



}
