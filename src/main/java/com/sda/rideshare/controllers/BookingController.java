package com.sda.rideshare.controllers;

import com.sda.rideshare.entities.*;
import com.sda.rideshare.repositories.BookingRepository;
import com.sda.rideshare.repositories.CarRepository;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class BookingController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

@Autowired
    private BookingRepository bookingRepository;
@Autowired
    private RideRepository rideRepository;
@Autowired
    private UserRepository userRepository;
@Autowired
private CarRepository carRepository;


    @PostMapping("/booking-save")
    public ModelAndView saveBooking(@ModelAttribute("booking")BookingModel bookingModel,
                                    @RequestParam(value = "rideId") Integer rideId,
                                    @RequestParam(value = "bookedSeats") Integer bookedSeats,
                                    BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/booked-ride");
        Optional<User> user = getLoggedInUser();
        UserEntity userEntity = null;
        if (user.isPresent()) {
            String username = user.get().getUsername();
            userEntity = userRepository.getUserByUsername(username);

        }

        RideEntity rideEntity = rideRepository.findById(rideId).get();
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setBookingDate(localDate);
        bookingEntity.setBookingTime(localTime);
        bookingEntity.setUser(userEntity);
        bookingEntity.setRide(rideEntity);
        bookingEntity.setBookedSeats(bookedSeats);
        bookingRepository.save(bookingEntity);
        Integer newAvailableSeats = rideEntity.getAvailableSeats() - bookedSeats;
        rideEntity.setAvailableSeats(newAvailableSeats);
        return modelAndView;

    }

    @GetMapping("/booked-ride")
    public ModelAndView bookRide(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("booked-ride");
        RideEntity rideEntity = rideRepository.findById(id).get();
        modelAndView.addObject("selectedRide", rideEntity);
        UserEntity userEntity = rideEntity.getUser();
        Integer userId = userEntity.getUserId();
        modelAndView.addObject("selectedDriver",userRepository.findById(userId).get());
        CarEntity carEntity = rideEntity.getCarEntity();
        Integer carId = carEntity.getCarId();
        modelAndView.addObject("selectedCar", carRepository.findById(carId).get());
        List<BookingEntity> bookingList = userEntity.getBookingList();
        BookingEntity bookingEntity = userEntity.getBookingList().get(bookingList.size()-1);
        modelAndView.addObject("booking", bookingEntity);
        return modelAndView;
    }
}
