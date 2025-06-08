package com.demo.deploy.service;

import com.demo.deploy.mapper.RoleMapper;
import com.demo.deploy.mapper.UserMapper;
import com.demo.deploy.model.Role;
import com.demo.deploy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public User createUser(User user) {
        // Mã hóa password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Lưu user
        userMapper.insert(user);

        // Gán role USER mặc định
        Role userRole = roleMapper.findByName("ROLE_USER");
        if (userRole != null) {
            roleMapper.addRoleToUser(user.getId(), userRole.getId());
        }

        return user;
    }

    public boolean existsByUsername(String username) {
        return userMapper.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userMapper.findByEmail(email).isPresent();
    }

    public List<User> findAllUsers() {
        return userMapper.findAll();
    }
}