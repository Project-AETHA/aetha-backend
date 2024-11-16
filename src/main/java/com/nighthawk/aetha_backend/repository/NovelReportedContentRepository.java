package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.NovelReportedContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelReportedContentRepository extends MongoRepository<NovelReportedContent, String> {

}
