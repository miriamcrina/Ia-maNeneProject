package com.sda.rideshare.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "addresses")
public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    private String city;
    private String streetAndNumber;

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

    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public void setStreetAndNumber(String street) {
        this.streetAndNumber = street;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "addressId=" + addressId +
                ", city='" + city + '\'' +
                ", streetAndNumber='" + streetAndNumber + '\'' +
                ", rideBeginning=" + rideBeginning +
                ", rideEnd=" + rideEnd +
                '}';
    }
}
