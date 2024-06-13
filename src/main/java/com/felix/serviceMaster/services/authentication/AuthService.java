package com.felix.serviceMaster.services.authentication;

import com.felix.serviceMaster.dto.SignupRequestDTO;
import com.felix.serviceMaster.dto.UserDTO;

public interface AuthService {
    UserDTO signUpClient(SignupRequestDTO signupRequestDTO);

    Boolean presentByEmail(String email);

    UserDTO signUpCompany(SignupRequestDTO signupRequestDTO);
}
