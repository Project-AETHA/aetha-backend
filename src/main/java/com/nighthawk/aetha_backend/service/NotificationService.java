package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.NotificationDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Notification;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.NotificationRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    //? Creates a notification
    //? Depending on the type, must send an email as well
    public boolean createNotification(NotificationDTO notificationDTO) {

        try {
            notificationRepository.save(modelMapper.map(notificationDTO, Notification.class));

            logger.log(Level.FINE, "Notification created successfully");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating a notification - {}", e.getMessage());
            return false;
        }

        return true;
    }


    public boolean createAnnouncement(NotificationDTO notificationDTO) {

        try {

            //? Creating a new notificationDTO using the previously built function
            createNotification(notificationDTO);

            //? Sending via the Kafka Template to the Channel "Announcements"
            kafkaTemplate.send("announcements", notificationDTO);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating a notification - {}", e.getMessage());
            return false;
        }

        return true;
    }

    //? Retrieve all the notification for a user - returns a response entity
    public ResponseDTO getNotificationsForUser(UserDetails userDetails) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

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

    public void markNotificationAsRead(String notificationId) {
        try {
            Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
            notification.setSeen(true);
            notificationRepository.save(notification);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error marking notification as read - {}", e.getMessage());
        }
    }
}
