package com.sda.rideshare.controllers;

import com.sda.rideshare.entities.CarEntity;
import com.sda.rideshare.entities.ModelRide;
import com.sda.rideshare.entities.UserEntity;
import com.sda.rideshare.repositories.CarRepository;
import com.sda.rideshare.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CarController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;


//    @GetMapping("/my-car")
//    public ModelAndView getMyCarPage () {
//        ModelAndView modelAndView = new ModelAndView("my-car");
//        return modelAndView;
//    }

    @GetMapping("/add-car")
    public ModelAndView addCar () {
        ModelAndView modelAndView = new ModelAndView("car-form");
        modelAndView.addObject("car", new CarEntity());
        return modelAndView;
    }

    @PostMapping("/car-save")
    public ModelAndView saveCar(@ModelAttribute("car") CarEntity carEntity, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/my-car");
        Optional<User> user = getLoggedInUser();
        UserEntity userEntity = null;
        if (user.isPresent()) {
            String username = user.get().getUsername();
            userEntity = userRepository.getUserByUsername(username);

        }
        carEntity.setUser(userEntity);
        carRepository.save(carEntity);
        return modelAndView;
    }

    @GetMapping("/my-car")
    public ModelAndView getCars() {
        ModelAndView modelAndView = new ModelAndView("my-car");
        modelAndView.addObject("carList", carRepository.findAll());
        return modelAndView;

    }

    @GetMapping("/edit-car/{id}")
    public ModelAndView editCategory (@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("car-form");
        modelAndView.addObject("car", carRepository.findById(id).get());
        return modelAndView;
    }

    @GetMapping("/delete-car/{id}")
    public ModelAndView deleteCategory (@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/my-car");
        carRepository.deleteById(id);
        return modelAndView;
    }

}
