package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.EbookRecord;
import com.nighthawk.aetha_backend.entity.ebook.EbookExternal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EbookRecordRepository extends MongoRepository<EbookRecord, String> {
    Optional<EbookRecord> findByEbookAndUser(EbookExternal ebook, AuthUser user);

    List<EbookRecord> findAllByUser(AuthUser user);
}
