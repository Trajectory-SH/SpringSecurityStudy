package com.trajectory.jwtBASIC.service;

import com.trajectory.jwtBASIC.dto.JoinDTO;
import com.trajectory.jwtBASIC.entity.UserEntity;
import com.trajectory.jwtBASIC.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        //만약 DB에 회원정보가 존재한다면(회원가입 id 중복) return
        if (isExist) {
            return;
        }

        /*
        * Spring은 앞단에 해당 접두사를 사용한뒤에 우리가 원하는 권한을 적어넣으면 된다.
        * 비밀번호는 항상 암호화해서 넣어야한다.
        * */
        UserEntity data = new UserEntity();
        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_ADMIN");

        userRepository.save(data);
    }
}
