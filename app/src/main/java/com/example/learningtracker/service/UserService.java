package com.example.learningtracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.learningtracker.entity.User;
import com.example.learningtracker.repository.UserRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(User user, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    public User updateWithoutPassword(User user, String password) {
        user.setPassword(password);
        return userRepository.save(user);
    }

    public User update(User user, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
}
