package com.example.jwtuser.controller;

import com.example.jwtuser.domain.User;
import com.example.jwtuser.security.UserPrincipal;
import com.example.jwtuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }
    @GetMapping("profile")
    public User getProfile(@AuthenticationPrincipal UserPrincipal user) {
        return userService.findById(user.getId());
    }
}
