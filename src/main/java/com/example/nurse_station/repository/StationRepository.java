package com.example.nurse_station.repository;

import com.example.nurse_station.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
}