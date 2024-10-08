package com.pomegranate.pomemusic.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pomegranate.pomemusic.dto.LoginDto;
import com.pomegranate.pomemusic.dto.RegisterDto;
import com.pomegranate.pomemusic.dto.ResponseDto;
import com.pomegranate.pomemusic.dto.ResponseUserDto;
import com.pomegranate.pomemusic.dto.UserDto;
import com.pomegranate.pomemusic.jwt.TokenService;
import com.pomegranate.pomemusic.model.Role;
import com.pomegranate.pomemusic.model.User;
import com.pomegranate.pomemusic.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository repository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ResponseUserDto> login(LoginDto body) {
        User user = this.repository.findByEmail(body.email()).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseUserDto(null, "", "User not found"));
        }
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseUserDto(new UserDto(user.getName(), 
            user.getEmail(), user.getUsername(), user.getYearOfBirth(), 
            user.getAvatar(), user.getFavoriteGenre()), token, "The user was found"));
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseUserDto(null, "", "Wrong password"));
        }
    }

    public ResponseEntity<ResponseDto> register(RegisterDto body) {

        if (this.repository.existsByEmail(body.email())) {
            return ResponseEntity.badRequest().body(new ResponseDto("Email already exists", ""));
        }

        if (this.repository.existsByUsername(body.username())) {
            return ResponseEntity.badRequest().body(new ResponseDto("Username already exists", ""));
        }
        
        User user = new User();
        user.setEmail(body.email());
        user.setPassword(passwordEncoder.encode(body.password()));
        user.setRole(Role.USER);
        user.setName(body.name());
        user.setUsername(body.username());
        user.setYearOfBirth(body.yearOfBirth());
        user.setAvatar(body.avatar());
        user.setFavoriteGenre("");
        this.repository.save(user);
        String token = "";
        return ResponseEntity.ok(new ResponseDto(user.getName(), token));
    }
}