package com.PostGreSQL.repository;

import com.PostGreSQL.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}

