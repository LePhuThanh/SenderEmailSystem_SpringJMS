package com.lpt.spring_jms.services.Impl;

import com.lpt.spring_jms.dto.LoginDto;
import com.lpt.spring_jms.dto.RegisterDto;
import com.lpt.spring_jms.entities.User;
import com.lpt.spring_jms.payloads.response.DataResponse;
import com.lpt.spring_jms.repositories.UserRepository;
import com.lpt.spring_jms.services.UserService;
import com.lpt.spring_jms.util.EmailUtil;
import com.lpt.spring_jms.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(RegisterDto registerDto)  {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
        } catch (MessagingException e){
            throw new RuntimeException("Unable to send otp please try again!");
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setOtp(otp);
        user.setOtpGenerateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public Boolean verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGenerateTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try{
            emailUtil.sendOtpEmail(email, otp);
        }catch (MessagingException e ){
            throw new RuntimeException("Unable to send otp please try again!");
        }
        user.setOtp(otp);
        user.setOtpGenerateTime(LocalDateTime.now());
        userRepository.save(user);
        return true;
//        return "Email send ... sent please verify account within 1 minute";
    }

    @Override
    public Boolean login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + loginDto.getEmail()));
        if (!loginDto.getPassword().equals(user.getPassword())){
            System.out.println("Password is incorrect");
            return  false; //Password is incorrect
        }else if (!user.isActive()){
            System.out.println("Your account is not verified");
            return false; //Your account is not verified
        }
        return true;
    }
}
