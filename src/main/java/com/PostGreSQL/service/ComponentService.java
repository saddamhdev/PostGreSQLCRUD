package com.PostGreSQL.service;


import com.PostGreSQL.model.ComponentEntity;
import com.PostGreSQL.repository.ComponentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService {
    private final ComponentRepository componentRepository;

    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    public ComponentEntity save(ComponentEntity component) {
        return componentRepository.save(component);
    }

    public List<ComponentEntity> findAll() {
        return componentRepository.findAll();
    }

    public void delete(Long id) {
        componentRepository.deleteById(id);
    }
}
