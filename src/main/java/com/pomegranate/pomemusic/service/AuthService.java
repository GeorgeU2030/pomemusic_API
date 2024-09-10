package com.pomegranate.pomemusic.service;

import com.pomegranate.pomemusic.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pomegranate.pomemusic.dto.LoginDto;
import com.pomegranate.pomemusic.dto.RegisterDto;
import com.pomegranate.pomemusic.dto.ResponseDto;
import com.pomegranate.pomemusic.jwt.JwtService;
import com.pomegranate.pomemusic.model.Role;
import com.pomegranate.pomemusic.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authentication;

    public ResponseDto login(LoginDto login){
        try {     
            authentication.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));                                                                                                               
            UserDetails userDetails = userRepository.findByEmail(login.getEmail()).orElseThrow();
            String token = jwtService.generateToken(userDetails);
            return ResponseDto.builder()
                .token(token)
                .build();
        } catch (Exception e) {
            System.err.println("Authentication failed for user: " + login.getEmail() + " with error: " + e.getMessage());
            throw new RuntimeException("Invalid username or password");
        }
    }

    public ResponseDto register(RegisterDto register){

        User user = User.builder()
            .username(register.getUsername())
            .password(passwordEncoder.encode(register.getPassword()))
            .email(register.getEmail())
            .name(register.getName())
            .role(Role.USER)
            .build();

        userRepository.save(user);

        return ResponseDto.builder()
            .token(jwtService.generateToken(user))
            .build();
    }
}
