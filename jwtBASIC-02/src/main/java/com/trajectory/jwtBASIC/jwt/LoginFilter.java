package com.trajectory.jwtBASIC.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//로그인 처리 필터 동작 시작

    private final AuthenticationManager authenticationManager;

    @Override
    //인증 객체를 return
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //provider manager(알맞는 Authentication provider를 is support? 를 통해서 반복문을 돌며 찾아준다)
        // -> DaoAuthenticationProvider(AbstractUserDetailsAuthenticationProvider) -> authenticate(retrieveUser) -> 추상화된 클래스에서 공통 로직 진행 + 훅...


        /*
          사용자의 요청에서 username과 password를 꺼내올 수 있다.
          AbstractAuthenticationProcessingFilter에 해당 메서드가 정의되어있다.
          request로부터 유저의 정보를 꺼내서 dto(UsernamePasswordAuthenticationToken)에 담에서
          Authentication Manager에게 던져준다. -> 해당 클래스에서 로그인 처리가 일어남
         */
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        log.info("=====login Member=====");
        log.info("[username = {} || password = {}]", username, password);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        //authenticationManager에 유저 정보가 담겨있는 토큰을 던져주면 알아서 로그인 과정을 진행한다.-> Authentication객체를 반환해준다.
        //그러면 Security Context의 생성 시점은 언제가 되는 것이지?
        return authenticationManager.authenticate(authToken);
    }


    /*try {
        // 여기서 attemptAuthentication 호출!
        authResult = attemptAuthentication(request, response);
        if (authResult == null) {
            return;
        }
        successfulAuthentication(request, response, chain, authResult);
    }
    catch (AuthenticationException failed) {
        unsuccessfulAuthentication(request, response, failed);
        return;
    }*/
    @Override
    //AbstractAuthenticationProcessingFilter(로그인 필터의 부모) -> authenticationManager에서 검증 성공후 Authentication 객체를을 성공적으로 만들어내면 실행한다.
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //로그인 성공시에 JWT 토큰을 발급해주는 로직을 작성하면 된다.


    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {


    }
}
