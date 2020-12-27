package com.sda.rideshare.repositories;

import com.sda.rideshare.entities.RideEntity;
import com.sda.rideshare.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface RideRepository extends JpaRepository<RideEntity, Integer> {
    RideEntity getRideByUser (UserEntity userEntity);
}
