package com.demo.deploy.controller;

import com.demo.deploy.model.JwtRequest;
import com.demo.deploy.model.JwtResponse;
import com.demo.deploy.model.RefreshTokenRequest;
import com.demo.deploy.model.User;
import com.demo.deploy.security.CustomUserDetails;
import com.demo.deploy.security.JwtUtil;
import com.demo.deploy.security.CustomUserDetailsService;
import com.demo.deploy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class JwtAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {
        System.out.println("Login request: " + authenticationRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Tên đăng nhập hoặc mật khẩu không đúng", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // Lấy thông tin user
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        User user = customUserDetails.getUser();

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken, user.getUsername(), user.getEmail(), user.getFullName()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtUtil.isTokenValid(refreshToken)) {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newToken = jwtUtil.generateToken(userDetails);

            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            User user = customUserDetails.getUser();

            return ResponseEntity.ok(new JwtResponse(newToken, refreshToken, user.getUsername(), user.getEmail(), user.getFullName()));
        }

        return ResponseEntity.badRequest().body("Invalid refresh token");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Kiểm tra username đã tồn tại
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
            }

            // Kiểm tra email đã tồn tại
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest().body("Email đã được sử dụng!");
            }

            User newUser = userService.createUser(user);
            return ResponseEntity.ok("Đăng ký thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra trong quá trình đăng ký!");
        }
    }
}