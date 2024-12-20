package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.SupportTicket;
import com.nighthawk.aetha_backend.utils.StatusList;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface SupportTicketRepository extends MongoRepository<SupportTicket, String> {
    List<SupportTicket> findByAuthor(AuthUser user);

    long countByStatus(StatusList status);

}
