package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.dto.NovelReportedContentSummaryDTO;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.NovelReportedContent;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelReportedContentRepository extends MongoRepository<NovelReportedContent, String> {

    @Aggregation(pipeline = {
            "{ '$group': { " +
                    "  '_id': '$novel.id', " +
                    "  'novel': { '$first': '$novel' }, " +
                    "  'reason': { '$last': '$reason' }, " +
                    "  'count': { '$sum': 1 }, " +
                    "  'createdAt': { '$max': '$createdAt' }" +
                    "} }",
            "{ '$sort': { 'count': -1 } }"  // Sort by number of reports in descending order
    })
    List<NovelReportedContentSummaryDTO> groupReportsByNovel();

    List<NovelReportedContent> findByNovel(Novel novel);

}
