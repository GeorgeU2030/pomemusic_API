package com.pomegranate.pomemusic.service;

import com.pomegranate.pomemusic.model.User;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

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

    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto login) {
        try {
            // Search user for email
            UserDetails userDetails = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    
            // Authenticate the user
            authentication.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
    
            // Generate the token JWT
            String token = jwtService.generateToken(userDetails);
    
            // Return the token - 200 OK
            return ResponseEntity.ok(ResponseDto.builder().token(token).build());
    
        } catch (ResponseStatusException e) {
            // Specific error for user not found
            return ResponseEntity.status(e.getStatusCode()).body(ResponseDto.builder().token(e.getReason()).build());
            
        } catch (BadCredentialsException e) {
            // Specific error for invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().token("Invalid username or password").build());
    
        } catch (Exception e) {
            // Generic error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().token("An unexpected error occurred").build());
        }
    }

    public ResponseEntity<ResponseDto> register(RegisterDto register) {

        // Check if username already exists
        if (userRepository.findByUsername(register.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ResponseDto.builder()
                    .token("Username already exists")
                    .build());
        }
    
        // Check if email already exists
        if (userRepository.findByEmail(register.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ResponseDto.builder()
                    .token("Email already exists")
                    .build());
        }
    
        // Create new user
        User user = User.builder()
            .username(register.getUsername())
            .password(passwordEncoder.encode(register.getPassword()))
            .email(register.getEmail())
            .name(register.getName())
            .role(Role.USER)
            .build();
    
        userRepository.save(user);
    
        // Return the JWT token - 201 Created
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseDto.builder()
                .token(jwtService.generateToken(user))
                .build());
    }
    
}
