package com.demo.deploy.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {
    public String getUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.isNull(auth) ? auth.getName() : null;
    }
}
