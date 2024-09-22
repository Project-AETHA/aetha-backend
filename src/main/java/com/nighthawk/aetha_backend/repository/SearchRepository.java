package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.dto.EbookExternalDTO;

import java.util.List;

public interface SearchRepository {
    List<EbookExternalDTO> searchEbooks(String searchTerm, List<String> genres, Double rating);
}
