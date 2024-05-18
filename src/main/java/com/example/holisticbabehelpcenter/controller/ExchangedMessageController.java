package com.example.holisticbabehelpcenter.controller;
import com.example.holisticbabehelpcenter.model.ExchangedMessages;
import com.example.holisticbabehelpcenter.model.User;
import com.example.holisticbabehelpcenter.service.ExchangedMessagesService;
import com.example.holisticbabehelpcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/ExchangedMessages")
public class ExchangedMessageController {
    @Autowired
    private ExchangedMessagesService exchangedMessagesService;
    @Autowired
    private UserService userService;
    @GetMapping("/exchanged/{userId}/{receiverId}")
    public ResponseEntity<List<ExchangedMessages>> getExchangedMessages(@PathVariable String userId, @PathVariable String receiverId) {
        try {
            User sender = userService.getUserByEmail(userId);
            User receiver = userService.getUserByEmail(receiverId);
            if (sender == null || receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            List<ExchangedMessages> sentMessages = exchangedMessagesService.findBySenderAndReceiver(sender, receiver);
            List<ExchangedMessages> receivedMessages = exchangedMessagesService.findByReceiverAndSender(receiver, sender);
            List<ExchangedMessages> exchangedMessages = new ArrayList<>(sentMessages);
            exchangedMessages.addAll(receivedMessages);
            exchangedMessages.sort(Comparator.comparing(ExchangedMessages::getSentTime));
            if (!exchangedMessages.isEmpty()) {
                System.out.println("First message content: " + exchangedMessages.get(0).getContent());
            }
            return ResponseEntity.ok(exchangedMessages);
        } catch (Exception e) {
            System.err.println("Error fetching exchanged messages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
