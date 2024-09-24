package com.pomegranate.pomemusic.controller;

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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login (@Valid @RequestBody LoginDto login){
        return this.authService.login(login);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register (@Valid @RequestBody RegisterDto register){
        return this.authService.register(register);
    }

}
