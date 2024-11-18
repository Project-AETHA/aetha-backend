package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Message;
import com.nighthawk.aetha_backend.entity.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByReceiverAndStatus(String receiver, MessageStatus status);
    List<Message> findBySenderAndStatus(String sender, MessageStatus status);
}
