package com.demo.aerztekasse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.aerztekasse.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}