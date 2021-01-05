package com.sda.rideshare.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "rides")
public class RideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rideId;

    private String departureCity;
    private String departureStreetAndNumber;
    private String arrivalCity;
    private String arrivalStreetAndNumber;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime departureTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime arrivalTime;

    private Integer passengerNumber;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "carId")
    private CarEntity carEntity;

//    @OneToOne
//    @JoinColumn(name = "cityDeparture", referencedColumnName = "city")
//    private AddressEntity departureAddress;
//
//    @OneToOne
//    @JoinColumn(name = "cityArrival", referencedColumnName = "city")
//    private AddressEntity arrivalAddress;

     public RideEntity() {
    }

    public CarEntity getCarEntity() {
        return carEntity;
    }

    public void setCarEntity(CarEntity carEntity) {
        this.carEntity = carEntity;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureStreetAndNumber() {
        return departureStreetAndNumber;
    }

    public void setDepartureStreetAndNumber(String departureStreetAndNumber) {
        this.departureStreetAndNumber = departureStreetAndNumber;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getArrivalStreetAndNumber() {
        return arrivalStreetAndNumber;
    }

    public void setArrivalStreetAndNumber(String arrivalStreetAndNumber) {
        this.arrivalStreetAndNumber = arrivalStreetAndNumber;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(Integer passengerNumber) {
        this.passengerNumber = passengerNumber;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
