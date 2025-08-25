package com.trajectory.jwtBASIC.service;

import com.trajectory.jwtBASIC.dto.CustomUserDetails;
import com.trajectory.jwtBASIC.entity.UserEntity;
import com.trajectory.jwtBASIC.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //데이터베이스에서 특정 유저를 조회해서 UserDetails에 담아서 return해주면 된다.
        UserEntity userData = userRepository.findByUsername(username);
        if (userData != null) {
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
