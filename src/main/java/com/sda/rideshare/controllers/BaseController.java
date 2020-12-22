package com.sda.rideshare.controllers;

import com.sda.rideshare.entities.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public abstract class BaseController {
    public Optional<User> getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(null != auth && auth.getPrincipal() instanceof User) {
            User user = (User)auth.getPrincipal();
            System.out.println(user.getUsername());
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
