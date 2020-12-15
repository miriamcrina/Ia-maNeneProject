package com.sda.rideshare.entities;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    private String city;
    private String street;
    private Integer number;

    @OneToOne (mappedBy = "departureAddress")
    private RideEntity rideBeginning;

    @OneToOne (mappedBy = "arrivalAddress")
    private RideEntity rideEnd;



    public AddressEntity() {
    }

    public RideEntity getRideBeginning() {
        return rideBeginning;
    }

    public void setRideBeginning(RideEntity rideBeginning) {
        this.rideBeginning = rideBeginning;
    }

    public RideEntity getRideEnd() {
        return rideEnd;
    }

    public void setRideEnd(RideEntity rideEnd) {
        this.rideEnd = rideEnd;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
