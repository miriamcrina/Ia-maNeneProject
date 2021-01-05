package com.sda.rideshare.entities;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carId;

    private String carMake;
    private String carModel;
    private String plateNumber;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @OneToOne(mappedBy = "carEntity")
    private RideEntity rideEntity;

    public RideEntity getRideEntity() {
        return rideEntity;
    }

    public void setRideEntity(RideEntity rideEntity) {
        this.rideEntity = rideEntity;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
