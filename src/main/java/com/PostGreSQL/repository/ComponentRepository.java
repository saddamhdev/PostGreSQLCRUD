package com.PostGreSQL.repository;


import com.PostGreSQL.model.ComponentEntity;
import com.PostGreSQL.model.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComponentRepository extends JpaRepository<ComponentEntity, Long> {
    List<ComponentEntity> findByPage(PageEntity page);  // ✅ add this
}
