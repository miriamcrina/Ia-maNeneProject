package com.sda.rideshare.repositories;

import com.sda.rideshare.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
    List<BookingEntity> getAllByBookingDateBetween(LocalDate startDate, LocalDate endDate);
}
