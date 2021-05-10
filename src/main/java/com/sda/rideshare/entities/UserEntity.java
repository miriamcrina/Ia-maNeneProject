package com.sda.rideshare.entities;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name= "users")
public class UserEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Email
    private String email;

    @NotBlank(message = "Campul nu poate fi gol")
    @Pattern(regexp="^\\p{L}+(?: \\p{L}+)*$",message = "Date incorecte")
    @Size(min = 3, max = 15, message = "Camp invalid - text prea lung sau prea scurt")
    private String surname;

    @NotBlank(message = "Campul nu poate fi gol")
    @Pattern(regexp="^\\p{L}+(?: \\p{L}+)*$",message = "Date incorecte")
    @Size(min = 3, max = 15, message = "Camp invalid - text prea lung sau prea scurt")
    private String name;

    @Past(message = "Data introdusa este incorecta.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateOfBirth;

    @NotBlank(message = "Campul nu poate fi gol")
    @Pattern(regexp = "[0-9]+", message = "Nr de telefon invalid")
    @Size(min = 10, max = 13, message = "Camp invalid - nr de telefon incorect")
    private String phoneNumber;

    @NotBlank(message = "Campul nu poate fi gol")
    private String username;

    private Boolean enabled;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate registrationDate;

    @NotBlank(message = "Campul nu poate fi gol")
    private String password;
    @OneToMany(mappedBy = "user")
    private List<AuthorityEntity> authorities;

    @OneToMany(mappedBy = "user")
    private List<RideEntity> rides;

    @OneToMany(mappedBy = "user")
    private List<CarEntity> cars;

    @OneToMany(mappedBy = "user")
    private  List<BookingEntity> bookingList;



    public UserEntity() {
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<BookingEntity> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<BookingEntity> bookingList) {
        this.bookingList = bookingList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CarEntity> getCars() {
        return cars;
    }

    public void setCars(List<CarEntity> cars) {
        this.cars = cars;
    }

    public List<RideEntity> getRides() {
        return rides;
    }

    public void setRides(List<RideEntity> rides) {
        this.rides = rides;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<AuthorityEntity> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityEntity> authorities) {
        this.authorities = authorities;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
