package com.sda.rideshare.repositories;
import com.sda.rideshare.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity getUserByUsername (String username);
    List<UserEntity> getAllByUsername (String username);
    List<UserEntity> getAllByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);
}
