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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.time.LocalDate;
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
    public ModelAndView saveRide(@Valid @ModelAttribute("modelRide") RideEntity rideEntity, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/main");

        if(bindingResult.hasErrors()) {
            modelAndView.setViewName("ride-form");
            Optional<User> user = getLoggedInUser();
            UserEntity userEntity = null;
            Integer id = null;
            if (user.isPresent()) {
                String username = user.get().getUsername();
                userEntity = userRepository.getUserByUsername(username);
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
        modelAndView.addObject("user", userRepository.findById(id).get());
        return modelAndView;

    }

//    @GetMapping("/edit-ride/{id}")
//    public ModelAndView editRide (@PathVariable Integer id) {
//        ModelAndView modelAndView = new ModelAndView("ride-form");
//        Optional<User> user = getLoggedInUser();
//        Integer userId = null;
//        if (user.isPresent()) {
//            String username = user.get().getUsername();
//            UserEntity userEntity = userRepository.getUserByUsername(username);
//            id = userEntity.getUserId();
//        }
//        modelAndView.addObject("user", userRepository.findById(userId).get());
//        modelAndView.addObject("modelRide", rideRepository.findById(id).get());
//        return modelAndView;
////       /........????????????????????????????????????????????????????????????????????????????
//    }

    @GetMapping("/delete-ride/{id}")
    public ModelAndView deleteRide(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/my-rides");
        RideEntity rideEntity = rideRepository.findById(id).get();
        List<BookingEntity> bookingList = rideEntity.getBookingList();
        bookingList.forEach(bookingEntity -> bookingEntity.getUser().getEmail());
        List<String> emails = bookingList.stream().map(be-> be.getUser().getEmail()).collect(Collectors.toList());
        emails.add(rideEntity.getUser().getEmail());
        bookingRepository.deleteAll(bookingList);
        rideRepository.deleteById(id);
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        try{
//            InternetAddress[] internetAddresses = new InternetAddress[emails.size()];
//            for (int i = 0; i < emails.size(); i++) {
//                internetAddresses[i] = new InternetAddress(emails.get(i));
//            }
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//            mimeMessageHelper.setFrom("noreply@baeldung.com");
//            mimeMessageHelper.setTo(internetAddresses);
//            mimeMessageHelper.setSubject("Notificare - Ia-ma, Nene!");
////            mimeMessageHelper.setText("Stimate client,\n " +
////                    "Cursa de la "+ rideEntity.getDepartureCity() + " la " + rideEntity.getArrivalCity()+ " din data de "+ rideEntity.getDepartureDate()+ " a fost anulata de catre "+
////                    rideEntity.getUser().getName()+ ".\n Pentru alte curse pe acelasi traseu, va rugam sa verificati site-ul nostru.\n"+
////                    " Numai bine,\n " +
////                    " Familia Ia-maNene ");
//            mimeMessage.setContent("Stimate client,<br> " +
//                    "Cursa de la "+ rideEntity.getDepartureCity() + " la " + rideEntity.getArrivalCity()+ " din data de "+ rideEntity.getDepartureDate()+ " a fost anulata de catre "+
//                    rideEntity.getUser().getName()+ ".<br> Pentru alte curse pe acelasi traseu, va rugam sa verificati site-ul nostru.<br>"+
//                    " Numai bine,<br> " +
//                    " Familia Ia-maNene ", "text/html");
//            javaMailSender.send(mimeMessage);
//        }catch (Exception e) {
//            e.getMessage();
//        }
        mailService.sendEmail(emails, mailService.getContent(rideEntity));

        return modelAndView;
    }



}
