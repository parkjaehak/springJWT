package com.jaehak.springjwt.config;

import com.jaehak.springjwt.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //1. AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    //2. AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        AuthenticationManager authenticationManager = configuration.getAuthenticationManager();
        return authenticationManager;
    }

    // 입력받은 비번을 db에 저장하기전에 암호화해야함
    // 해당 메소드 구현필요
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

         //jwt에서는 session을 stateless상태로 관리해 csrf공격방어 불필요
        //csrf disable
        http
                .csrf(CsrfConfigurer::disable);


        /**
         * jwt방식 로그인 진행할 것임
         *
         *  form 로그인 방식을 disable시켰기 때문에
         *  UsernamePasswordAuthenticationFilter,
         *  AuthenticationManager 따로 구현필요
         */

        // form 로그인 방식 disable
        http
                .formLogin(FormLoginConfigurer::disable);

        //http basic 인증 방식 disable
        http
                .httpBasic(HttpBasicConfigurer::disable);

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()); //이외에는 로그인한 사용자만 접근 가능

        //세션 설정(stateless로 만드는 것이 가장 중요)
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                //3.LoginFilter에 autenticationManager주입
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
