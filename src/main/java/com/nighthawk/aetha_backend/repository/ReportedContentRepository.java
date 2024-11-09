package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.ReportedContent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportedContentRepository extends MongoRepository <ReportedContent, String> {

}
