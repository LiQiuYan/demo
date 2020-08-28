package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class SimpleController {

    @GetMapping("/hello")
    public String hello() {
        return "hello,this is a springboot demo~";
    }

}
