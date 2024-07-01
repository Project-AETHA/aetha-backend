package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.SupportTicket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupportTicketRepository extends MongoRepository<SupportTicket, String> {
    public SupportTicket findByAuthor(String email);
}
