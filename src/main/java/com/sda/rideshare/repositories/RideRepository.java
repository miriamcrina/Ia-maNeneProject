package com.sda.rideshare.repositories;

import com.sda.rideshare.entities.RideEntity;
import com.sda.rideshare.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<RideEntity, Integer> {

    List<RideEntity> getAllByDepartureCityAndArrivalCityAndDepartureDate(String departureCity, String arrivalCity, LocalDate departureDate );
    List<RideEntity> getAllByDepartureDateBetween (LocalDate startDate, LocalDate endDate);

}
