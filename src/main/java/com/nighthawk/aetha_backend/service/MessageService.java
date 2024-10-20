package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.MessageDTO;
import com.nighthawk.aetha_backend.entity.Message;
import com.nighthawk.aetha_backend.entity.MessageStatus;
import com.nighthawk.aetha_backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<MessageDTO> getInboxMessages(String receiver) {
        List<Message> messages = messageRepository.findByReceiverAndStatus(receiver, MessageStatus.INBOX);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getSentMessages(String sender) {
        List<Message> messages = messageRepository.findBySenderAndStatus(sender, MessageStatus.SENT);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getDraftMessages(String sender) {
        List<Message> messages = messageRepository.findBySenderAndStatus(sender, MessageStatus.DRAFT);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getTrashMessages(String user) {
        List<Message> receivedTrash = messageRepository.findByReceiverAndStatus(user, MessageStatus.TRASH);
        List<Message> sentTrash = messageRepository.findBySenderAndStatus(user, MessageStatus.TRASH);
        receivedTrash.addAll(sentTrash); // Combine both received and sent trash messages
        return receivedTrash.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getTitle(),
                message.getSender(),
                message.getReceiver(),
                message.getDateTime()
        );
    }
}
