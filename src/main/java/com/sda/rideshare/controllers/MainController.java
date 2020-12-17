package com.sda.rideshare.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public ModelAndView getFrontPage () {
        ModelAndView modelAndView = new ModelAndView("front-page");
        return modelAndView;
    }

    @GetMapping("/main")
    public ModelAndView getMainPage () {
        ModelAndView modelAndView = new ModelAndView("main-page");
        return modelAndView;
    }
}
