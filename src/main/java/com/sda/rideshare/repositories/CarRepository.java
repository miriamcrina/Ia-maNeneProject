package com.sda.rideshare.repositories;

import com.sda.rideshare.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Integer> {
List<CarEntity> getAllByAddDateBetween(LocalDate startDate, LocalDate endDate);
}
