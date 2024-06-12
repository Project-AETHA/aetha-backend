package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.Blog;
import com.nighthawk.aetha_backend.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogRepository repository;

    public List<Blog> getAllBlogs() {
        return repository.findAll();
    }

}
