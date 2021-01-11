package com.sda.rideshare.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carId;

    @NotBlank(message = "Campul nu poate fi gol")
    @Pattern(regexp="^[A-Za-z]*$",message = "Date incorecte")
    @Size(min = 3, max = 10, message = "Camp invalid - text prea lung sau prea scurt")
    private String carMake;

    @NotBlank(message = "Campul nu poate fi gol")
    @Size(min = 2, max = 15, message = "Camp invalid - text prea lung sau prea scurt")
    private String carModel;

    @NotBlank(message = "Campul nu poate fi gol")
    @Size(min = 7, max = 10, message = "Camp invalid - text prea lung sau prea scurt")
    private String plateNumber;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @OneToMany(mappedBy = "carEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RideEntity> rides;

    public List<RideEntity> getRides() {
        return rides;
    }

    public void setRides(List<RideEntity> rides) {
        this.rides = rides;
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
