package com.lpt.spring_jms.controllers;

import com.lpt.spring_jms.dto.LoginDto;
import com.lpt.spring_jms.dto.RegisterDto;
import com.lpt.spring_jms.entities.User;
import com.lpt.spring_jms.payloads.response.DataResponse;
import com.lpt.spring_jms.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping(value = "/register")
    public ResponseEntity<DataResponse> register(@RequestBody RegisterDto registerDto) throws MessagingException {
        User user = userService.register(registerDto);
        if (user != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new DataResponse("200","User registration successfully", user)
            );
        }
        throw new RuntimeException("User registration failed");
    }
    @PutMapping(value = "/verify-account")
    public ResponseEntity<DataResponse> verifyAccount(@RequestParam String email, String otp ){
       Boolean isVerified = userService.verifyAccount(email, otp);
        if (isVerified){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new DataResponse("200","OTP verified you can login")
            );
        }
            throw new RuntimeException("Please regenerate otp and try again!");
    }
    @PutMapping(value = "/regenerate-otp")
    public ResponseEntity<DataResponse> regenerateOtp(@RequestParam String email){
        Boolean isRegenerateOtp = userService.regenerateOtp(email);
        if (isRegenerateOtp){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new DataResponse("200","Email send ... sent please verify account within 1 minute")
            );
        }
        throw new RuntimeException("Regenerate otp failed and try again!");
    }
    @PostMapping(value = "/login")
    public ResponseEntity<DataResponse> login(@RequestBody LoginDto loginDto){
        Boolean isLogin = userService.login(loginDto);
        if (isLogin){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new DataResponse("200","Login successfully")
            );
        }
        throw new RuntimeException("You password is incorrect or your account is not verified!");
    }

}

//https://www.youtube.com/watch?v=kBtgDVzjFzs