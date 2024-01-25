package com.jaehak.springjwt.controller;

import com.jaehak.springjwt.dto.JoinDto;
import com.jaehak.springjwt.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProcess(@ModelAttribute JoinDto joinDto) {

        log.info("username={}, password={}", joinDto.getUsername(), joinDto.getPassword());

        try {
            joinService.joinProcess(joinDto);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException occurred: {}", e.getMessage(), e);
            return "bad";
        }

        return "ok"; //원래는 회원가입이 정상적으로 되었을때만 상태코드 ok보내야함
    }
}
