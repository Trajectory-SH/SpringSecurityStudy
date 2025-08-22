package com.trajectory.jwtBASIC.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    //UsernamePasswordAuthenticationFilter를 상속받아서 customFilter를 만들 때, 해당 메서드를 반드시 구현해야한다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //UsernamePasswordAuthenticationFilter 클래스에 request로부터 username고 password를 가져오는 메서드가 구현되어있다.
        //AuthenticationManager(인증 로직 수행)에게 username/password를 던져준다 -> 그냥 던져주면 안되고 사전에 정의된 DTO에 담아서 던져줘야함
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("["+username +"_____" +password+"]");


        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //인증매니저가 DB에서 회원 정보를 당겨와서 UserDetailsService를 통해 유저 정보를 받고 검증을 진행한다.
        //인증 성공, 실패에 따라서 아래에 정의해 놓은 메서드가 실행된다.
        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("success");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("fail");
    }
}
