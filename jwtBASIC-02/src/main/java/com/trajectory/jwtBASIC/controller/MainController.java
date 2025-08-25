package com.trajectory.jwtBASIC.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {

    @GetMapping("/")
    public String mainP() {

        //String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("======================");
        //log.info("user Principal = {}", principal);
        log.info("user name = {}", username);
        log.info("======================");

        return "Main Controller";
    }
}
