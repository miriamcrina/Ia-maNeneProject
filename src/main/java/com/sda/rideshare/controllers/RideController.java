package com.sda.rideshare.controllers;

import com.sda.rideshare.entities.*;
import com.sda.rideshare.repositories.BookingRepository;
import com.sda.rideshare.repositories.CarRepository;
import com.sda.rideshare.repositories.RideRepository;
import com.sda.rideshare.repositories.UserRepository;
import com.sda.rideshare.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class RideController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RideController.class);

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MailService mailService;

    @GetMapping("/rides/add")
    public ModelAndView addRide() {
        ModelAndView modelAndView = new ModelAndView("ride-form");
        Optional<User> user = getLoggedInUser();
        Integer id = null;
        if (user.isPresent()) {
            String username = user.get().getUsername();
            UserEntity userEntity = userRepository.getUserByUsername(username);
            id = userEntity.getUserId();
        }
        modelAndView.addObject("user", userRepository.findById(id).get());
        modelAndView.addObject("modelRide", new RideEntity());
        return modelAndView;
    }
    @PostMapping("/rides/save")
    public ModelAndView saveRide( @Valid @ModelAttribute("modelRide") RideEntity rideEntity, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/my-rides");
        if(bindingResult.hasErrors()) {
            modelAndView.setViewName("ride-form");
            Optional<User> user = getLoggedInUser();
            Integer id = null;
            if (user.isPresent()) {
                String username = user.get().getUsername();
                UserEntity  userEntity = userRepository.getUserByUsername(username);
                id = userEntity.getUserId();
            }
            rideEntity.setAvailableSeats(rideEntity.getPassengerNumber());
            modelAndView.addObject("user", userRepository.findById(id).get());
            modelAndView.addObject("modelRide", rideEntity);
            return modelAndView;
        }
        Optional<User> user = getLoggedInUser();
        UserEntity userEntity = null;
        if (user.isPresent()) {
            String username = user.get().getUsername();
            userEntity = userRepository.getUserByUsername(username);
        }
        rideEntity.setAvailableSeats(rideEntity.getPassengerNumber());
        rideEntity.setUser(userEntity);
        rideRepository.save(rideEntity);
        return modelAndView;
    }

    @GetMapping("/find-ride")
    public ModelAndView getRides() {
        ModelAndView modelAndView = new ModelAndView("find-ride");
        return modelAndView;
    }

    @GetMapping("/found-rides")
    public ModelAndView getFoundRides(@RequestParam(value = "departureCity") String departureCity,
                                      @RequestParam(value = "arrivalCity") String arrivalCity,
                                      @RequestParam(value = "departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate) {

        ModelAndView modelAndView = new ModelAndView("found-rides");
        List<RideEntity> rides = rideRepository.getAllByDepartureCityAndArrivalCityAndDepartureDate(departureCity, arrivalCity, departureDate);
        rides.removeIf(ride -> ride.getAvailableSeats() <= 0);
        modelAndView.addObject("foundRides", rides);
        return modelAndView;
    }


    @GetMapping("/select-ride/{id}")
    public ModelAndView selectRide(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("selected-ride");
        RideEntity rideEntity = rideRepository.findById(id).get();
        modelAndView.addObject("selectedRide", rideEntity);
        UserEntity userEntity = rideEntity.getUser();
        Integer userId = userEntity.getUserId();
        modelAndView.addObject("selectedDriver",userRepository.findById(userId).get());
        CarEntity carEntity = rideEntity.getCarEntity();
        Integer carId = carEntity.getCarId();
        modelAndView.addObject("selectedCar", carRepository.findById(carId).get());
        modelAndView.addObject("booking", new BookingModel());
        return modelAndView;
    }

    @GetMapping("/my-rides")
    public ModelAndView getHistoryRides() {
        ModelAndView modelAndView = new ModelAndView("rides");
        Optional<User> user = getLoggedInUser();
        Integer id = null;
        if (user.isPresent()) {
            String username = user.get().getUsername();
            UserEntity userEntity = userRepository.getUserByUsername(username);
            id = userEntity.getUserId();
        }
        Date dateToConvert = java.util.Calendar.getInstance().getTime();;
        LocalDate localDate = dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        modelAndView.addObject("localDate", localDate);
        modelAndView.addObject("user", userRepository.findById(id).get());
        return modelAndView;

    }



    @GetMapping("/delete-ride/{id}")
    public ModelAndView deleteRide(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/my-rides");
        RideEntity rideEntity = rideRepository.findById(id).get();
        List<BookingEntity> bookingList = rideEntity.getBookingList();
        List<String> emails = bookingList.stream().map(be-> be.getUser().getEmail()).collect(Collectors.toList());
        emails.add(rideEntity.getUser().getEmail());
        bookingRepository.deleteAll(bookingList);
        rideRepository.deleteById(id);
        mailService.sendEmail(emails, mailService.getContent(rideEntity));

        return modelAndView;
    }


    @GetMapping("/ride-statistic")
    public ModelAndView getAllRides() {
        ModelAndView modelAndView = new ModelAndView("ride-statistic-form");
        return modelAndView;
    }

    @GetMapping("/ride-report")
    public ModelAndView getAllFoundRides(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ModelAndView modelAndView = new ModelAndView("ride-report");
        List<RideEntity> rides = rideRepository.getAllByDepartureDateBetween(startDate, endDate);
        modelAndView.addObject("allRides", rides);
        return modelAndView;
    }


}
