package com.jaehak.springjwt.service;

import com.jaehak.springjwt.domain.UserEntity;
import com.jaehak.springjwt.dto.JoinDto;
import com.jaehak.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto) {

        String username = joinDto.getUsername();
        String password = joinDto.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        // 회원존재하면 메소드 강제 종료 (boolean return해서 controller단에서 사용자에게 메세지보내도됨)
        if (isExist) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }

        UserEntity user = new UserEntity();

        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password)); // 암호화해서 save해야함
        user.setRole("ROLE_ADMIN"); // 회원가입하는 모든 사람 일단 admin줌

        log.info("username={}, password={}, role={}", user.getUsername(), user.getPassword(), user.getRole());

        userRepository.save(user);
    }
}
