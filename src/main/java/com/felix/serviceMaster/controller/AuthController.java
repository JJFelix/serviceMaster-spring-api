package com.felix.serviceMaster.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.felix.serviceMaster.dto.AuthenticationRequest;
import com.felix.serviceMaster.dto.SignupRequestDTO;
import com.felix.serviceMaster.dto.UserDTO;
import com.felix.serviceMaster.entity.User;
import com.felix.serviceMaster.repository.UserRepository;
import com.felix.serviceMaster.services.authentication.AuthService;
import com.felix.serviceMaster.services.jwt.UserDetailsServiceImpl;
import com.felix.serviceMaster.util.JwtUtil;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import netscape.javascript.JSException;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public static final String TOKEN_PREFIX = "Bearer";

    public static final String HEADER_STRING = "Authorization";

    @PostMapping("/client/signup")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO) {
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            return new ResponseEntity<>("Client with this email already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO createdUser = authService.signUpClient(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/company/signup")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO) {
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            return new ResponseEntity<>("Company with this email already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO createdUser = authService.signUpCompany(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/authenticate")    
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
    HttpServletResponse response) throws JSException, IOException, java.io.IOException, JSONException{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e)      {
            throw new BadCredentialsException("Incorrect username or Password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());

        response.getWriter().write(new JSONObject()
        .put("userId", user.getId())
        .put("role", user.getRole())
        .toString());

        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Headers", "Authorization, " + "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-Header");

        response.addHeader(HEADER_STRING, TOKEN_PREFIX+jwt);
    }
}
