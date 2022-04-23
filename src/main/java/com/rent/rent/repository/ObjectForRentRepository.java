package com.rent.rent.repository;

import com.rent.rent.model.ObjectForRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectForRentRepository extends JpaRepository<ObjectForRent, Long> {
}
