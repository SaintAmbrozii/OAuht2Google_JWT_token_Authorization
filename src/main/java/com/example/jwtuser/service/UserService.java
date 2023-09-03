package com.example.jwtuser.service;

import com.example.jwtuser.domain.User;
import com.example.jwtuser.exception.NotFoundException;
import com.example.jwtuser.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
    public User save(User user) {
        return userRepo.save(user);
    }
    public List<User> findAll() {
        return userRepo.findAll();
    }
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
