package com.users.services;

import com.clothes.model.entitis.Product;
import com.clothes.services.ProductService;
import com.exceptions.AppException;
import com.users.model.Role;
import com.users.model.RoleName;
import com.users.model.User;
import com.security.payload.ApiResponse;
import com.security.payload.JwtAuthenticationResponse;
import com.security.payload.LoginRequest;
import com.users.model.likes.GetLike;
import com.users.model.registration.ChangePassword;
import com.users.model.registration.RegisterUser;
import com.users.repositories.RoleRepository;
import com.users.repositories.UserRepository;
import com.security.JwtTokenProvider;
import io.vavr.control.Try;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    private ProductService productService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<?> newPassword(HttpServletRequest request, ChangePassword changePassword) {
        Long id = (Long) request.getSession().getAttribute("id_user");
        String oldPassword = changePassword.getPassword();
        String newPassword = changePassword.getNewPassword();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to find the user in the repository"));

        boolean userExists = !user.getEmail().equals("") && !user.getHash().equals("");

        boolean changePasswordNull = oldPassword != null &&
                passwordEncoder.matches(oldPassword, user.getHash());
        boolean passwordRequirements = changePassword.checkNewPassword();
        boolean samePassword = changePassword.samePassword();

        if (changePasswordNull && passwordRequirements && samePassword && userExists) {
            String generatedSecuredPasswordHash = passwordEncoder.encode(newPassword);
            user.setHash(generatedSecuredPasswordHash);
            userRepository.save(user);

            return new ResponseEntity<>(new ApiResponse(true, "User password changed successfully"),
                    HttpStatus.OK);

        } else if (!changePasswordNull) {
            return new ResponseEntity<>("1 Bad old password",
                    HttpStatus.BAD_REQUEST);
        } else if (!passwordRequirements) {
            return new ResponseEntity<>("2 Password does not meet the requirements",
                    HttpStatus.BAD_REQUEST);
        } else if (!samePassword) {
            return new ResponseEntity<>("3 Passwords don't match",
                    HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("4 Something went wrong",
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

        if (letLogIn) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getEmail(),
                            login.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            long idUser = existingUser.getId();

            String jwt = tokenProvider.generateToken(authentication);
            httpServletRequest.getSession(true).setAttribute("email", email);
            httpServletRequest.getSession(true).setAttribute("id_user", idUser);
            tokenProvider.store(jwt, authentication);


            return new ResponseEntity<>(new JwtAuthenticationResponse(jwt), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Login failed entirely"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> createUser(RegisterUser registerUser) {
        String email = registerUser.getEmail();
        String password = registerUser.getPassword();

        User existingUser = userRepository.findByEmail(email).orElse(new User("", ""));

        boolean userExists = !existingUser.getEmail().equals("") && !existingUser.getHash().equals("");
        boolean registerEmailNull = email != null && !userExists;
        boolean samePassword = registerUser.samePassword();
        boolean passwordRequirements = registerUser.checkPassword();

        if (registerEmailNull && samePassword && passwordRequirements) {
            String hashedPassword = passwordEncoder.encode(password);

            updateUser(hashedPassword, email);

//            URI location = ServletUriComponentsBuilder
//                    .fromCurrentContextPath().path("/api/users/{email}")
//                    .buildAndExpand(result.getEmail()).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/auth/"));

            return new ResponseEntity<>(
                    "User registered successfully", headers, HttpStatus.MOVED_PERMANENTLY);

        } else if (userExists) {
            return new ResponseEntity<>(new ApiResponse(false, "1 Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        } else if (!passwordRequirements) {
            return new ResponseEntity<>(new ApiResponse(false, "3 Password does not meet the requirements"),
                    HttpStatus.BAD_REQUEST);
        } else if (!samePassword) {
            return new ResponseEntity<>(new ApiResponse(false, "2 Passwords aren't matching"),
                    HttpStatus.BAD_REQUEST);
        }  else {
            return new ResponseEntity<>(new ApiResponse(false, "User creation failed"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void updateUser(String password, String email) {
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        User user = new User(email, password);
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
    }

    public List<User> GetAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> like(HttpServletRequest httpServletRequest, GetLike getLike) {
        Long idProduct = getLike.getIdProduct();
        Long idUser = (Long) httpServletRequest.getSession(true).getAttribute("id_user");
        User user = userRepository.findById(idUser).orElse(new User("", ""));
        Product product = productService.getProductById(idProduct);
        user.addProductToLikes(product);

        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<?> removeLike(HttpServletRequest httpServletRequest, GetLike getLike) {
        Long idProduct = getLike.getIdProduct();
        Long idUser = (Long) httpServletRequest.getSession(true).getAttribute("id_user");
        User user = userRepository.findById(idUser).orElse(new User("", ""));
        Product product = productService.getProductById(idProduct);
        user.removeProductLike(product);

        userRepository.deleteLike(idProduct, idUser);
        return ResponseEntity.ok("");
    }

    public ResponseEntity<?> favourite(HttpServletRequest httpServletRequest, GetLike getLike) {
        Long idProduct = getLike.getIdProduct();
        Long idUser = (Long) httpServletRequest.getSession(true).getAttribute("id_user");
        User user = userRepository.findById(idUser).orElse(new User("", ""));
        Product product = productService.getProductById(idProduct);
        user.addProductToFavourites(product);

        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<?> removeFavourite(HttpServletRequest httpServletRequest, GetLike getLike) {
        Long idProduct = getLike.getIdProduct();
        Long idUser = (Long) httpServletRequest.getSession(true).getAttribute("id_user");
        User user = userRepository.findById(idUser).orElse(new User("", ""));
        Product product = productService.getProductById(idProduct);
        user.removeProductFavourite(product);

        userRepository.deleteFavourite(idProduct, idUser);
        return ResponseEntity.ok("");
    }

    public String logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().invalidate();
        return "redirect:/api/auth/";
    }
}
