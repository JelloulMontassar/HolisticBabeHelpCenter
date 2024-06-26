package com.example.holisticbabehelpcenter.service;

import com.example.holisticbabehelpcenter.model.Notification;
import com.example.holisticbabehelpcenter.model.Reclamation;
import com.example.holisticbabehelpcenter.model.User;
import com.example.holisticbabehelpcenter.repository.ReclamationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReclamationReminderService {

    @Autowired
    private ReclamationRepository reclamationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private EntityManager entityManager;
    @Transactional
    @Scheduled(cron = "0 0 8 * * *")
    //@Scheduled(fixedRate = 20000)
    public void remindAdminOfUnresolvedReclamations() {
        List<Reclamation> unresolvedReclamations = reclamationRepository.findByResult("Not resolved");
        if (!unresolvedReclamations.isEmpty()) {
            User user2 = userService.getUserByEmail("admin@admin.com");
            user2 = entityManager.merge(user2);
            Notification notification = new Notification();
            notification.setReceiver(user2);
            notification.setStatus("Reminder");
            notification.setMessage(("You Have " + unresolvedReclamations.size() + " Reclamations not solved"));
            notification.setSeen(false);
            LocalDate currentDate = LocalDate.now();
            notification.setSendDate(Date.valueOf(currentDate));
            notificationService.sendNotification(notification);
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        }

    }
}

