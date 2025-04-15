package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.repositories.UserRepository;;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAddress(email);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}
