package com.lpt.spring_jms.services;

import com.lpt.spring_jms.dto.LoginDto;
import com.lpt.spring_jms.dto.RegisterDto;
import com.lpt.spring_jms.entities.User;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    User register(RegisterDto registerDto) throws MessagingException;

    Boolean verifyAccount(String email, String otp);

    Boolean regenerateOtp(String email);

    Boolean login(LoginDto loginDto);
}
