package com.sda.rideshare.controllers;

import com.sda.rideshare.entities.*;
import com.sda.rideshare.repositories.AddressRepository;
import com.sda.rideshare.repositories.RideRepository;
import com.sda.rideshare.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class RideController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(RideController.class);

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/rides/add")
    public ModelAndView addRide() {
        ModelAndView modelAndView = new ModelAndView("ride-form");
        modelAndView.addObject("modelRide", new ModelRide());
        return modelAndView;
    }

    @PostMapping("/rides/save")
    public ModelAndView saveRide(@ModelAttribute("modelRide") ModelRide modelRide, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/main");
        Optional<User> user = getLoggedInUser();
        UserEntity userEntity = null;
        if(user.isPresent()){
            String username = user.get().getUsername();
          userEntity = userRepository.getUserByUsername(username);

        }
        RideEntity rideEntity = new RideEntity();
        AddressEntity departureAddress = new AddressEntity();
        departureAddress.setCity(modelRide.getDepartureCity());
        departureAddress.setStreetAndNumber(modelRide.getDepartureStreetAndNumber());
        addressRepository.save(departureAddress);

        AddressEntity arrivalAddress = new AddressEntity();
        arrivalAddress.setCity(modelRide.getArrivalCity());
        arrivalAddress.setStreetAndNumber(modelRide.getArrivalStreetAndNumber());
        addressRepository.save(arrivalAddress);

        rideEntity.setRideId(modelRide.getModelRideId());
        rideEntity.setDepartureAddress(departureAddress);
        rideEntity.setArrivalAddress(arrivalAddress);
        rideEntity.setDepartureDate(modelRide.getDepartureDate());
        rideEntity.setDepartureTime(modelRide.getDepartureTime());
        rideEntity.setArrivalTime(modelRide.getArrivalTime());
        rideEntity.setPassengerNumber(modelRide.getPassengerNumber());
        rideEntity.setUser(userEntity);

        rideRepository.save(rideEntity);
        return modelAndView;
    }

//    @GetMapping("/find-ride")
//    public ModelAndView getMainPage () {
//        ModelAndView modelAndView = new ModelAndView("find-ride");
//        return modelAndView;
//    }
 @GetMapping("/view-rides/{id}")
 public ModelAndView getHistoryRides( @PathVariable Integer id) {
     ModelAndView modelAndView = new ModelAndView("rides");
      HistoryRideModel historyRideModel = new HistoryRideModel();

     UserEntity userEntity = userRepository.findById(id).get();
     modelAndView.addObject("historyList", new HistoryRideModel());
     RideEntity rideEntity = rideRepository.getRideByUser(userEntity);
     historyRideModel.setDepartureCity(rideEntity.getDepartureAddress().getCity());
     historyRideModel.setArrivalCity(rideEntity.getArrivalAddress().getCity());
     historyRideModel.setDate(rideEntity.getDepartureDate());
    return modelAndView;

}

    @GetMapping("/delete-ride/{id}")
    public ModelAndView deleteCategory (@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/view-rides");
        rideRepository.deleteById(id);
        return modelAndView;
    }

}
