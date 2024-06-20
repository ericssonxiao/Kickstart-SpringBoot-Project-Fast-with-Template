package com.eric.demo.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.eric.xml.todoapp.User;

import jakarta.annotation.PostConstruct;

@Component
public class UserRepository {

    private static final Map<String, User> users = new HashMap<>();

    public User findUser(String firstName) {
        return users.get(firstName);
    }

    @PostConstruct
    public void initData() {
        User appuser = new User();
        appuser.setId(1);
        appuser.setFirstName("Jack");
        appuser.setLastName("Happo");
        appuser.setEmail("jack@gmail.com");
        appuser.setPassword("123456");
        appuser.setLocation("Montreal");
        appuser.setCreatedAt("2024-06-19");
        users.put(appuser.getFirstName(), appuser);

        // userRepository.save(new User(1, "jack", "Happo", "jack@gmail.com", "123456",
        // "Montreal", "2024-06-19"));
        // userRepository.save(new User(2, "tom", "Happo", "tom@gmail.com", "123456",
        // "USA", "2024-06-19"));
        // userRepository.save(new User(3, "Lucy", "Happo", "lucy@gmail.com", "123456",
        // "Quebec", "2024-06-19"));
    }
}
