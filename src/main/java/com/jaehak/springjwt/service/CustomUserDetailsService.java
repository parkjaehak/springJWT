package com.jaehak.springjwt.service;

import com.jaehak.springjwt.domain.UserEntity;
import com.jaehak.springjwt.dto.CustomUserDetails;
import com.jaehak.springjwt.repository.UserRepository;
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

        UserEntity findUser = userRepository.findByUsername(username);

        // db에서 찾아온 user검증
        if (findUser != null) {
            return new CustomUserDetails(findUser);
        }

        return null;
    }
}
