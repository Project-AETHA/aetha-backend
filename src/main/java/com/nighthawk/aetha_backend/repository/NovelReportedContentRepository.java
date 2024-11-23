package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.dto.NovelReportedContentSummaryDTO;
import com.nighthawk.aetha_backend.entity.NovelReportedContent;
import com.nighthawk.aetha_backend.entity.Novel;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelReportedContentRepository extends MongoRepository<NovelReportedContent, String> {

    @Aggregation(pipeline = {
            // Lookup to join with the 'novels' collection (the novel field is now a DBReference)
            "{ '$lookup': { 'from': 'novels', 'localField': 'novel', 'foreignField': '_id', 'as': 'novelDetails' } }",
            "{ '$unwind': '$novelDetails' }",  // Unwind the results to flatten them
            // Group by novel ID, counting the reports and keeping track of other details
            "{ '$group': { " +
                    "  '_id': '$novelDetails._id', " +
                    "  'title': { '$first': '$novelDetails.title' }, " +
                    "  'reason': { '$first': '$reason' }, " +
                    "  'count': { '$sum': 1 }, " +
                    "  'createdAt': { '$max': '$createdAt' }" +
                    "} }",
            // Sort by count of reports in descending order
            "{ '$sort': { 'count': -1 } }"
    })
    List<NovelReportedContentSummaryDTO> groupReportsByNovel();

    // Method to find reports by specific novel
    List<NovelReportedContent> findByNovel(Novel novel);
}
