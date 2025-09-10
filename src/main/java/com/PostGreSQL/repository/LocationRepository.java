package com.PostGreSQL.repository;

import com.PostGreSQL.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByDivision(String division);
}
