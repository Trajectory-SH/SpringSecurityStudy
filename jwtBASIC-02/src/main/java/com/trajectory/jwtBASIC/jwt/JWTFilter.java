package com.trajectory.jwtBASIC.jwt;

import com.trajectory.jwtBASIC.dto.CustomUserDetails;
import com.trajectory.jwtBASIC.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Request의 Header에서 authorization 항목을 뽑아온다. -> JWT 토큰
        String authorization = request.getHeader("Authorization");
        /**
         * 어떤 메서드를 실행하는 경우에 검증로직/예외로직을 먼저 상단에 작성한 후에
         * 이후 정상 로직을 실행시키는 과정이 예외상황에 관련해서 자연스럽고 확인하기도 쉽다.
         */
        //JWT 토큰에 대한 검증 실행 1
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("===[Token is Null]===");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메서드 종료 토큰이 검증되지 않았는데 계속해서 아래 로직을 진행해버리면 검증 성공한 이후의 로직이 실행되어버린다.
            return;
        }

        String token = authorization.split(" ")[1];//Bearer(소유자) 분리 -> 이후 실제 인코딩 되어있는 JWT만 추출

        //JWT 토큰에 대한 검증 실행 2
        if (jwtUtil.isExpired(token)) {
            log.info("===token is Expired===");
            filterChain.doFilter(request, response);

            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //UserEntity를 생성해서 Security Context 생성
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setRole(role);
        userEntity.setPassword("temp");//유저의 비밀번호를 굳이 DB 에서 조회해서 받아올 필요가 없다. -> 어차피  토큰 클레임만 사용(완전 무상태)

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
