package com.sda.rideshare.controllers;

import com.sda.rideshare.entities.BookingEntity;
import com.sda.rideshare.entities.CarEntity;
import com.sda.rideshare.entities.UserEntity;
import com.sda.rideshare.repositories.CarRepository;
import com.sda.rideshare.repositories.UserRepository;
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
import java.util.List;
import java.util.Optional;

@Controller
public class CarController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/add-car")
    public ModelAndView addCar () {
        ModelAndView modelAndView = new ModelAndView("car-form");
        modelAndView.addObject("car", new CarEntity());
        return modelAndView;
    }

    @PostMapping("/car-save")
    public ModelAndView saveCar(@Valid @ModelAttribute("car") CarEntity carEntity, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/my-car");

        if(bindingResult.hasErrors()) {

            modelAndView.setViewName("car-form");
            carEntity.setAddDate(LocalDate.now());
            modelAndView.addObject("car", carEntity);
            return modelAndView;
        }

        Optional<User> user = getLoggedInUser();
        UserEntity userEntity = null;
        if (user.isPresent()) {
            String username = user.get().getUsername();
            userEntity = userRepository.getUserByUsername(username);

        }
        carEntity.setAddDate(LocalDate.now());
        carEntity.setUser(userEntity);
        carRepository.save(carEntity);
        return modelAndView;
    }

    @GetMapping("/my-car")
    public ModelAndView getCars() {
        ModelAndView modelAndView = new ModelAndView("my-car");

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

    @GetMapping("/edit-car/{id}")
    public ModelAndView editCar (@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("car-form");
        modelAndView.addObject("car", carRepository.findById(id).get());
        return modelAndView;
    }

    @GetMapping("/car-statistic")
    public ModelAndView getAllCars() {
        ModelAndView modelAndView = new ModelAndView("car-statistic-form");
        return modelAndView;
    }

    @GetMapping("/car-report")
    public ModelAndView getAllFoundCars(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ModelAndView modelAndView = new ModelAndView("car-report");
        List<CarEntity> cars = carRepository.getAllByAddDateBetween(startDate, endDate);
        modelAndView.addObject("allCars", cars);
        return modelAndView;
    }

}
