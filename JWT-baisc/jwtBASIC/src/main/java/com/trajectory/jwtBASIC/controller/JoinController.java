package com.trajectory.jwtBASIC.controller;

import com.trajectory.jwtBASIC.dto.JoinDTO;
import com.trajectory.jwtBASIC.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProcess(@ModelAttribute JoinDTO joinDTO) {

        joinService.joinProcess(joinDTO);
        return "Hello World";
    }
}
