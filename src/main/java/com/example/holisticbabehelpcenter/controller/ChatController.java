package com.example.holisticbabehelpcenter.controller;

import com.example.holisticbabehelpcenter.dto.Message;
import com.example.holisticbabehelpcenter.model.ExchangedMessages;
import com.example.holisticbabehelpcenter.model.User;
import com.example.holisticbabehelpcenter.service.ExchangedMessagesService;
import com.example.holisticbabehelpcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.Iterator;

@CrossOrigin("*")
@Controller
public class ChatController {
    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ExchangedMessagesService exchangedMessagesService;
    public ChatController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @MessageMapping("/private-message/{session}")
    public void handlePrivateMessage(@Payload Message message, @DestinationVariable("session") String user) {
        User sender = userService.getUserByEmail(message.getSenderName());
        User receiver = userService.getUserByEmail(message.getReceiverName());
        System.out.println("RECEIVED   ");
        ExchangedMessages savedMessage = new ExchangedMessages();
        savedMessage.setContent(message.getMessage());
        savedMessage.setReceiver(receiver);
        savedMessage.setSender(sender);
        savedMessage.setSentTime(LocalDateTime.now());
        ExchangedMessages savedMessage1 = exchangedMessagesService.saveMessage(savedMessage);
        message.setId(savedMessage1.getId());
        System.out.println("Received message: " + message);
        System.out.println("Receiver name: " + message.getReceiverName());
        messagingTemplate.convertAndSendToUser(user, "/private", message);
    }


}
