package com.pomegranate.pomemusic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pomegranate.pomemusic.dto.LoginDto;
import com.pomegranate.pomemusic.dto.RegisterDto;
import com.pomegranate.pomemusic.dto.ResponseDto;
import com.pomegranate.pomemusic.dto.ResponseUserDto;
import com.pomegranate.pomemusic.jwt.TokenService;
import com.pomegranate.pomemusic.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseUserDto> login (@Valid @RequestBody LoginDto login){
        return this.authService.login(login);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register (@Valid @RequestBody RegisterDto register){
        return this.authService.register(register);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token){
        String subject = tokenService.validateToken(token);
        if (subject != null){
            return ResponseEntity.ok("isValid");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
