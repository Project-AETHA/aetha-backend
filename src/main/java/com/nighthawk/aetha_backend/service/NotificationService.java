package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.NotificationDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Notification;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.NotificationRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

//    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AuthUserRepository userRepository;

    //? Creates a notification
    //? Depending on the type, must send an email as well
    public boolean createNotification(NotificationDTO notification) {

        try {
            Notification newNotification = new Notification();
            newNotification.setType(notification.getType());
            newNotification.setSubject(notification.getSubject());
            newNotification.setMessage(notification.getMessage());
            newNotification.setSeen(notification.getSeen());
            newNotification.setLink(notification.getLink());

            //? Getting the recipient from the email via user repository
            newNotification.setRecipient(userRepository.findByEmail(notification.getRecipient()).orElseThrow(() -> new RuntimeException("User not found")));

            notificationRepository.save(newNotification);

            logger.log(Level.FINE, "Notification created successfully");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating a notification - {}", e.getMessage());
            return false;
        }

        return true;
    }


    public boolean createAnnouncement(NotificationDTO notification) {

        try {

            //? Creating a new notification using the previously built function
            createNotification(notification);



        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating a notification - {}", e.getMessage());
            return false;
        }

        return true;
    }

    //? Retrieve all the notification for a user - returns a response entity
    public ResponseDTO getNotificationsForUser(String Email) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            AuthUser user = userRepository.findByEmail(Email).orElseThrow(() -> new RuntimeException("User not found"));

            List<Notification> notifications = notificationRepository.findByRecipient(user);

            responseDTO.setMessage("Notifications retrieved successfully");
            responseDTO.setContent(notifications);
            responseDTO.setCode(VarList.RSP_SUCCESS);

        } catch (RuntimeException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not found");
            responseDTO.setContent(null);
            responseDTO.setErrors(e.getMessage());

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Unknown error occurred");
            responseDTO.setContent(null);
            responseDTO.setErrors(e.getMessage());
            logger.log(Level.SEVERE, "Error retrieving notifications - {}", e.getMessage());

        }

        return responseDTO;
    }

}
