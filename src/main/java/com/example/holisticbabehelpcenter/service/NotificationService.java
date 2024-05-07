package com.example.holisticbabehelpcenter.service;

import com.example.holisticbabehelpcenter.model.Notification;
import com.example.holisticbabehelpcenter.model.User;
import com.example.holisticbabehelpcenter.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    public void sendNotification(Notification notification) {
        notificationRepository.save(notification);
    }
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    public List<Notification> getLatestNotifications(User user) {

        return notificationRepository.findBySeenFalseAndReceiverOrderBySendDateDesc(user);
    }
    public Notification getNotification(Long notificationId) {
        return notificationRepository.getById(notificationId);
    }
}
