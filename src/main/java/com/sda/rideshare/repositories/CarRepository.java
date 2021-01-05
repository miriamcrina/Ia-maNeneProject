package com.sda.rideshare.repositories;

import com.sda.rideshare.entities.CarEntity;
import com.sda.rideshare.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Integer> {

}
