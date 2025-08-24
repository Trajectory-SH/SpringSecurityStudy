package com.trajectory.jwtBASIC.jwt;

import com.trajectory.jwtBASIC.dto.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


        response.addHeader("Access-Control-Expose-Headers", "Authorization");

        // 응답 본문에도 토큰 추가 (선택사항)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String token = jwtUtil.createJwt(username, role, 60 * 60 * 10L);

        System.out.println("토큰 생성 완료: Bearer " + token);

        response.addHeader("Authorization", "Bearer " + token);
        String authorization = response.getHeader("Authorization");
        System.out.println(authorization);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);

    }
}
