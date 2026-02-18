package com.gpsolutions.taskhotels.repository;

import com.gpsolutions.taskhotels.entity.Amenity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

  Optional<Amenity> findByNameIgnoreCase(String name);

}
