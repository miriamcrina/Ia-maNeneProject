package com.sda.rideshare.controllers;

import com.sda.rideshare.entities.AuthorityEntity;
import com.sda.rideshare.entities.CarEntity;
import com.sda.rideshare.entities.UserEntity;
import com.sda.rideshare.repositories.AuthorityRepository;
import com.sda.rideshare.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public ModelAndView registerUser() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", new UserEntity());
        modelAndView.addObject("editMode", false);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUserRequest(@Valid @ModelAttribute("user") UserEntity userEntity, BindingResult bindingResult) {
        String username = userEntity.getUsername();
        List<UserEntity> users = userRepository.getAllByUsername(username);
        if (users!= null && !users.isEmpty()){
            bindingResult.addError(new FieldError("user", "username",userEntity.getUsername(), false, null, null, "Username already exists"));
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/login");
        if(bindingResult.hasErrors()){
            modelAndView.setViewName("register");
            userEntity.setRegistrationDate(LocalDate.now());
            modelAndView.addObject("user", userEntity);
            return modelAndView;
        }
            userEntity.setRegistrationDate(LocalDate.now());
            userEntity.setEnabled(true);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        userEntity = userRepository.save(userEntity);
        if (null == userEntity.getAuthorities()) {
            AuthorityEntity authorityEntity = new AuthorityEntity();
            authorityEntity.setUser(userEntity);
            authorityEntity.setUsername(userEntity.getUsername());
            authorityEntity.setAuthority("USER");
            authorityRepository.save(authorityEntity);
        }
        return modelAndView;

    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView("/login");
        return modelAndView;
    }

    @GetMapping("/login-error")
    public ModelAndView loginError(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView("login");
        String errorMessage = null;
        if (session != null) {
            Object object = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (object instanceof BadCredentialsException) {
                errorMessage = "Wrong username or password";
            }
        }
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

    @GetMapping("/user-statistic")
    public ModelAndView getAllUsers() {
        ModelAndView modelAndView = new ModelAndView("user-statistic-form");
        return modelAndView;
    }

    @GetMapping("/user-report")
    public ModelAndView getAllFoundCars(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                        @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ModelAndView modelAndView = new ModelAndView("user-report");
        List<UserEntity> users = userRepository.getAllByRegistrationDateBetween (startDate, endDate);
        modelAndView.addObject("allUsers", users);
        return modelAndView;
    }
}
