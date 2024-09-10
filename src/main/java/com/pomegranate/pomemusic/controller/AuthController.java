package com.pomegranate.pomemusic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pomegranate.pomemusic.dto.LoginDto;
import com.pomegranate.pomemusic.dto.RegisterDto;
import com.pomegranate.pomemusic.dto.ResponseDto;
import com.pomegranate.pomemusic.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<ResponseDto> login (@RequestBody LoginDto login){
        return authService.login(login);
    }

    @PostMapping(value = "register")
    public ResponseEntity<ResponseDto> register (@Valid @RequestBody RegisterDto register){
        return authService.register(register);
    }

}
