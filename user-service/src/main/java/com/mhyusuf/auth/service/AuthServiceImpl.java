package com.mhyusuf.auth.service;

import com.mhyusuf.auth.dto.LoginDto;
import com.mhyusuf.auth.dto.RegisterDto;
import com.mhyusuf.auth.entity.Role;
import com.mhyusuf.auth.entity.User;
import com.mhyusuf.auth.repository.RoleRepository;
import com.mhyusuf.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    public String register(RegisterDto registerDto) {

        // Cek apakah username atau email sudah digunakan
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("Username sudah digunakan!");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email sudah digunakan!");
        }

        // Buat user baru
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Set role default (USER)
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role tidak ditemukan"));
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        return "User berhasil didaftarkan!";
    }
}