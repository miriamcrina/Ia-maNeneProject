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
@Autowired
private MailService mailService;


    @PostMapping("/booking-save/{id}")
    public ModelAndView saveBooking(@ModelAttribute("booking")BookingModel bookingModel,
                                    @RequestParam(value = "bookedSeats") Integer bookedSeats,
                                    @PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/booked-ride/{id}");
        Optional<User> user = getLoggedInUser();
        UserEntity userEntity = null;
        if (user.isPresent()) {
            String username = user.get().getUsername();
            userEntity = userRepository.getUserByUsername(username);

        }

        RideEntity rideEntity = rideRepository.findById(id).get();
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        Integer newAvailableSeats = rideEntity.getAvailableSeats() - bookedSeats;
        rideEntity.setAvailableSeats(newAvailableSeats);
        rideRepository.save(rideEntity);
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setBookingDate(localDate);
        bookingEntity.setBookingTime(localTime);
        bookingEntity.setUser(userEntity);
        bookingEntity.setRide(rideEntity);
        bookingEntity.setBookedSeats(bookedSeats);
        bookingRepository.save(bookingEntity);
        mailService.sendEmail(rideEntity.getUser().getEmail(), mailService.getContentBooking(rideEntity, bookingEntity));
        return modelAndView;

    }

    @GetMapping("/booked-ride/{id}")
    public ModelAndView bookRide(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("booked-ride");
        RideEntity rideEntity = rideRepository.findById(id).get();
        modelAndView.addObject("selectedRide", rideEntity);
        UserEntity userEntity = rideEntity.getUser();
        modelAndView.addObject("selectedDriver", userEntity);
        Optional<User> user = getLoggedInUser();
        UserEntity connectedUser = null;
        if (user.isPresent()) {
            String username = user.get().getUsername();
            connectedUser = userRepository.getUserByUsername(username);

        }
        List<BookingEntity> list = connectedUser.getBookingList();
        BookingEntity entity;
        if (list.size() == 0){
           entity = connectedUser.getBookingList().get(list.size());
        } else{
         entity = connectedUser.getBookingList().get(list.size()-1);
        }
        modelAndView.addObject("booking", entity);
        return modelAndView;
    }

    @GetMapping("/delete-booking/{id}")
    public ModelAndView deleteBooking (@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/my-rides");
        BookingEntity bookingEntity = bookingRepository.findById(id).get();
        RideEntity rideEntity = bookingEntity.getRide();
        Integer newAvailableSeats = rideEntity.getAvailableSeats() + bookingEntity.getBookedSeats();
        rideEntity.setAvailableSeats(newAvailableSeats);
        rideRepository.save(rideEntity);
        bookingRepository.deleteById(id);
        mailService.sendEmail(rideEntity.getUser().getEmail(), mailService.getContentBookingCancellation(rideEntity, bookingEntity));
        return modelAndView;
    }


    @GetMapping("/booking-statistic")
    public ModelAndView getAllBookings() {
        ModelAndView modelAndView = new ModelAndView("booking-statistic-form");
        return modelAndView;
    }

    @GetMapping("/booking-report")
    public ModelAndView getAllFoundBookings(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                         @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ModelAndView modelAndView = new ModelAndView("booking-report");
        List<BookingEntity> bookings = bookingRepository.getAllByBookingDateBetween(startDate, endDate);
        modelAndView.addObject("allBookings", bookings);
        return modelAndView;
    }

}
