package com.eric.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.demo.domain.Users;

import com.eric.demo.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users")
    public Users createUser(@Valid @RequestBody Users newUser) {
        return userRepository.save(newUser);
    }

    @GetMapping("/users")
    public List<Users> getAll(@RequestParam(required = false, defaultValue = "") String param) {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<Users> getUser(@PathVariable int userId) {
        return userRepository.findById(userId);
    }

    @PutMapping("/users/{id}")
    public Users updateUser(@PathVariable Integer userId, @Valid @RequestBody Users newUser) {
        return userRepository.findById(userId)
                .<Users>map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setPassword(newUser.getPassword());
                    user.setLocation(newUser.getLocation());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setUserId(userId);
                    return userRepository.save(newUser);
                });

    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

}
