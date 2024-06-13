package com.felix.serviceMaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.felix.serviceMaster.dto.SignupRequestDTO;
import com.felix.serviceMaster.dto.UserDTO;
import com.felix.serviceMaster.services.authentication.AuthService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/client/signup")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO){
        if (authService.presentByEmail(signupRequestDTO.getEmail())){
            return new ResponseEntity<>("Client with this email already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO createdUser = authService.signUpClient(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/company/signup")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO){
        if (authService.presentByEmail(signupRequestDTO.getEmail())){
            return new ResponseEntity<>("Company with this email already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO createdUser = authService.signUpCompany(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

}
