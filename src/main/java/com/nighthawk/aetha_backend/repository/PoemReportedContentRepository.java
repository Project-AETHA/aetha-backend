package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.dto.PoemReportedContentSummaryDTO;
import com.nighthawk.aetha_backend.entity.Poem;
import com.nighthawk.aetha_backend.entity.PoemReportedContent;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoemReportedContentRepository extends MongoRepository<PoemReportedContent, String> {

    @Aggregation(pipeline = {
            "{ '$group': { " +
                    "  '_id': '$poem.id', " +
                    "  'poem': { '$first': '$poem' }, " +
                    "  'reason': { '$last': '$reason' }, " +
                    "  'count': { '$sum': 1 }, " +
                    "  'createdAt': { '$max': '$createdAt' }" +
                    "} }",
            "{ '$sort': { 'count': -1 } }"  // Sort by number of reports in descending order
    })
    List<PoemReportedContentSummaryDTO> groupReportsByPoem();

    List<PoemReportedContent> findByPoem(Poem poem);
}

