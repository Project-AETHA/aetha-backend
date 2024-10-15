package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.VoteRecords;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRecordRepository extends MongoRepository<VoteRecords, String> {

}
