package com.mhyusuf.auth.service;

import com.mhyusuf.auth.dto.LoginDto;
import com.mhyusuf.auth.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}