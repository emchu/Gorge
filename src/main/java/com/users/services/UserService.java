package com.users.services;

import com.exceptions.AppException;
import com.users.model.Role;
import com.users.model.RoleName;
import com.users.model.User;
import com.security.payload.ApiResponse;
import com.security.payload.JwtAuthenticationResponse;
import com.security.payload.LoginRequest;
import com.users.model.registration.ChangePassword;
import com.users.model.registration.RegisterUser;
import com.users.repositories.RoleRepository;
import com.users.repositories.UserRepository;
import com.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collections;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<?> newPassword(HttpServletRequest request, ChangePassword changePassword) {
        long id = Long.parseLong(request.getUserPrincipal().getName());
        String oldPassword = changePassword.getNewPassword();
        String newPassword = changePassword.getNewPassword2();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to find the user in the repository"));

        boolean changePasswordNull = oldPassword != null;

        if (changePasswordNull && changePassword.checkNewPassword()) {
            String generatedSecuredPasswordHash = passwordEncoder.encode(newPassword);
            user.setHash(generatedSecuredPasswordHash);
            userRepository.save(user);

            return new ResponseEntity<>(new ApiResponse(true, "User password changed successfully"),
                    HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>(new ApiResponse(false, "Failed to change the password"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, LoginRequest login) {
        String email = login.getEmail();
        String password = login.getPassword();
        User existingUser = userRepository.findByEmail(email)
                .orElse(new User("", ""));

        boolean letLogIn = email != null && password != null
                && !existingUser.getEmail().equals("") && !existingUser.getHash().equals("")
                && passwordEncoder.matches(login.getPassword(), existingUser.getHash());

        if(letLogIn){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getEmail(),
                            login.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            httpServletRequest.getSession(true).setAttribute("email", email);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Login failed entirely"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> createUser(RegisterUser registerUser)  {
        String email = registerUser.getEmail();
        String password = registerUser.getPassword();

        User existingUser = userRepository.findByEmail(email).orElse(new User("",""));

        boolean userExists = !existingUser.getEmail().equals("") && !existingUser.getHash().equals("");
        boolean registerEmailNull = email != null && !userExists;


        if (registerEmailNull) {
            String hashedPassword = passwordEncoder.encode(password);

            User result = updateUser(hashedPassword, email);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/api/users/{email}")
                    .buildAndExpand(result.getEmail()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "User registered successfully"));
        } else if (userExists) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "User creation failed"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private User updateUser(String password, String email){
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        User user = new User(email, password);
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    public List<User> GetAllUsers(){
        return userRepository.findAll();
    }
}
