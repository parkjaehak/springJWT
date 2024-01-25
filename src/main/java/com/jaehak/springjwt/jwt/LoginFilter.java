package com.jaehak.springjwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // authentication 진행하기위한 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String requestURI = request.getRequestURI();
        log.info("로그인 체크 필터 시작{}", requestURI);

        //== 요청을 가로챈다 ==//
        //클라이언트 요청에서 username, password 토큰 객체 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("username={},password={}", username, password);

        //== AuthenticationManager에 추출된 정보를 넘겨줌, 이때 dto(UsernamePasswordAuthenticationToken)사용 ==//
        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //== 실제 db와의 검증은 AuthenticationManager에서 이루어짐
        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        String requestURI = request.getRequestURI();
        log.info("로그인 성공={}", requestURI);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        String requestURI = request.getRequestURI();
        log.info("로그인 실패={}", requestURI);
    }
}
