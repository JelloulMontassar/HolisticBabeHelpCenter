package com.example.holisticbabehelpcenter.controller;
import com.example.holisticbabehelpcenter.model.Notification;
import com.example.holisticbabehelpcenter.model.User;
import com.example.holisticbabehelpcenter.service.NotificationService;
import com.example.holisticbabehelpcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @MessageMapping("/subscribe")
    @SendTo("/topic/notifications/")
    public List<Notification> subscribe(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return notificationService.getLatestNotifications(user);
    }


}

