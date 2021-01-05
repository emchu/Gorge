package com.users.controllers;

import com.users.model.User;
import com.security.payload.LoginRequest;
import com.users.model.likes.GetLike;
import com.users.model.registration.ChangePassword;
import com.users.model.registration.RegisterUser;
import com.users.services.UserService;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?>  createUser(@NotNull HttpServletRequest httpServletRequest, @RequestBody @NotNull RegisterUser registerUser) {
        return Try.of(() -> userService.createUser(registerUser)).getOrElseGet( t -> {
            log.error("Exception. {}", t.getCause().getMessage());
            return null;
        });
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.POST)
    public String logout(HttpServletRequest httpServletRequest) {
        return userService.logout(httpServletRequest);
    }

    @RequestMapping(value = "/users/signin", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?>  login( @NotNull HttpServletRequest httpServletRequest,
                                     @RequestBody @NotNull LoginRequest login) {
        return Try.of(() ->  userService.login(httpServletRequest, login)).getOrElseGet( t -> {
            log.error("Exception. {}", t.getCause().getMessage());
            return null;
        });
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/get-all")
    public List<User> GetAllUsers(){
        return userService.GetAllUsers();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/password/change")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> newPassword( @NotNull HttpServletRequest httpServletRequest,
                                          @RequestBody @NotNull ChangePassword changePassword) {
        return  Try.of(() -> userService.newPassword(httpServletRequest, changePassword)).getOrElseGet( t -> {
            log.error("Exception. {}", t.getCause().getMessage());
            return null;
        });
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/api/auth/like")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> likeProduct(@NotNull HttpServletRequest httpServletRequest,
                                         @RequestBody @NotNull GetLike getLike) {
        return Try.of(() -> userService.like(httpServletRequest, getLike)).getOrElseGet( t -> {
            log.error("Exception. {}", t.getCause().getMessage());
            return null;
        });
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/api/auth/like/remove")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> removeLikedProduct(@NotNull HttpServletRequest httpServletRequest,
                                                @RequestBody @NotNull GetLike getLike) {
        return Try.of(() -> userService.removeLike(httpServletRequest, getLike)).getOrElseGet( t -> {
            log.error("Exception. {}", t.getCause().getMessage());
            return null;
        });
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/api/auth/favourite")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> favouriteProduct(@NotNull HttpServletRequest httpServletRequest,
                                              @RequestBody @NotNull GetLike getLike) {
        return Try.of(() -> userService.favourite(httpServletRequest, getLike)).getOrElseGet( t -> {
            log.error("Exception. {}", t.getCause().getMessage());
            return null;
        });
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/api/auth/favourite/remove")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> removeFavouriteProduct(@NotNull HttpServletRequest httpServletRequest,
                                                    @RequestBody @NotNull GetLike getLike) {
        return Try.of(() -> userService.removeFavourite(httpServletRequest, getLike)).getOrElseGet( t -> {
            log.error("Exception. {}", t.getCause().getMessage());
            return null;
        });
    }
}