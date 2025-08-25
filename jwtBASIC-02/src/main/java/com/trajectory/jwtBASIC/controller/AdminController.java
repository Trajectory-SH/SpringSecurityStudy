package com.trajectory.jwtBASIC.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/")
    public String AdminP() {

        return "Admin Controller";
    }
}
