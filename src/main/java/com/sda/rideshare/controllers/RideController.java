package com.sda.rideshare.controllers;

import com.sda.rideshare.entities.AddressEntity;
import com.sda.rideshare.entities.RideEntity;
import com.sda.rideshare.repositories.RideRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
public class RideController {
    private static final Logger logger = LoggerFactory.getLogger(RideController.class);

    @Autowired
    private RideRepository rideRepository;

    @GetMapping("/rides/add")
    public ModelAndView ddCategory() {
        ModelAndView modelAndView = new ModelAndView("ride-form");
        modelAndView.addObject("address", new AddressEntity());
        modelAndView.addObject("ride", new RideEntity());
        return modelAndView;
    }

    @PostMapping("/rides/save")
    public  ModelAndView saveCategory( @ModelAttribute("ride") RideEntity rideEntity, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/main");
        rideRepository.save(rideEntity);

        return  modelAndView;
    }
}
