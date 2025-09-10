package com.PostGreSQL.repository;

import com.PostGreSQL.model.Menu;
import com.PostGreSQL.model.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageRepository extends JpaRepository<PageEntity, Long> {
    List<PageEntity> findByMenu(Menu menu);  // ✅ add this
}
