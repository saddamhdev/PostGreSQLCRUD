package com.PostGreSQL.service;

import com.PostGreSQL.model.PageEntity;
import com.PostGreSQL.repository.PageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PageService {
    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public PageEntity save(PageEntity page) {
        return pageRepository.save(page);
    }

    public List<PageEntity> findAll() {
        return pageRepository.findAll();
    }

    public void delete(Long id) {
        pageRepository.deleteById(id);
    }
}
