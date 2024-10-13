package com.nighthawk.aetha_backend.repository;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nighthawk.aetha_backend.dto.EbookExternalDTO;
import org.bson.Document;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ? Imports from the Atlas Search Index


@Component
public class SearchRepositoryImpl implements SearchRepository {

    @Autowired
    MongoClient mongoClient;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<EbookExternalDTO> searchEbooks(String searchTerm, List<String> genres, Double rating) {

        final List<EbookExternalDTO> ebooks = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("aetha_db");
        MongoCollection<Document> collection = database.getCollection("ebooks_external");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                        new Document("index", "ebookSearch")
                        .append("text",
                        new Document("query", searchTerm)
                        .append("path", Arrays.asList("title", "description", "cover_image")))),
                        new Document("$sort",
                        new Document("createdAt", -1L))));

        result.forEach(doc -> ebooks.add(modelMapper.map(doc, EbookExternalDTO.class)));

        return ebooks;
    }
}
