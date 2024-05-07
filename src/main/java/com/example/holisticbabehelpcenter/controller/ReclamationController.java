package com.example.holisticbabehelpcenter.controller;

import com.example.holisticbabehelpcenter.model.Notification;
import com.example.holisticbabehelpcenter.dto.ReclamationDTO;
import com.example.holisticbabehelpcenter.model.Reclamation;
import com.example.holisticbabehelpcenter.model.User;
import com.example.holisticbabehelpcenter.service.NotificationService;
import com.example.holisticbabehelpcenter.service.ReclamationService;
import com.example.holisticbabehelpcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/reclamations")
public class ReclamationController {
    @Autowired
    private ReclamationService reclamationService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService ;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reclamation>> getAllReclamationsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setUserId(userId);
        List<Reclamation> reclamations = reclamationService.getAllReclamationsByUser(user);
        return ResponseEntity.ok(reclamations);
    }
    @GetMapping("/")
    public List<Reclamation> getAllReclamations() {
        return reclamationService.getAllReclamations();
    }
    @GetMapping("/reclamation/{Id}")
    public ResponseEntity<ReclamationDTO> getReclamationById(@PathVariable Long Id) {
        Reclamation rec = reclamationService.getReclamationById(Id);
        ReclamationDTO recDTO = mapReclamationToDTO(rec);
        return new ResponseEntity<>(recDTO, HttpStatus.ACCEPTED);
    }
    @PostMapping("/reclamation/resolve/{Id}")
    public ResponseEntity<ReclamationDTO> resolveReclamation(@PathVariable Long Id) {
        Reclamation rec = reclamationService.getReclamationById(Id);
        rec.setResult("resolved");
        Reclamation updated = reclamationService.createReclamation(rec);
        ReclamationDTO recDTO = mapReclamationToDTO(updated);
        Notification notification = new Notification();
        notification.setReceiver(rec.getUser());
        notification.setStatus(rec.getStatus());
        notification.setMessage("Your reclamation has been resolved.");
        notification.setSendDate(Date.valueOf(rec.getReclamationDate()));
        notification.setReclamationId(rec.getId());
        notification.setSeen(false);
        String userId = rec.getUser().getUserId().toString();
        String destination = "/topic/notifications/";
        notificationService.sendNotification(notification);
        messagingTemplate.convertAndSend(destination, notification);
        return new ResponseEntity<>(recDTO, HttpStatus.ACCEPTED);
    }
    private ReclamationDTO mapReclamationToDTO(Reclamation rec) {
        ReclamationDTO recDTO = new ReclamationDTO();
        recDTO.setId(rec.getId());
        recDTO.setReclamationDate(rec.getReclamationDate());
        recDTO.setStatus(rec.getStatus());
        recDTO.setDescription(rec.getDescription());
        recDTO.setUserId(rec.getUser());
        recDTO.setResult(rec.getResult());
        recDTO.setScreenshot(rec.getScreenshot());
        return recDTO;
    }
    @PostMapping("/create/")
    public ResponseEntity<Reclamation> createReclamation(Authentication authentication , @RequestBody Reclamation reclamation) {
        User user  = userService.getUserByEmail(authentication.getName());
        System.out.println(user.getEmail());
        reclamation.setUser(user);
        reclamation.setResult("Not resolved");
        Reclamation createdReclamation = reclamationService.createReclamation(reclamation);
        Notification notification = new Notification();
        User user2 = userService.getUserById("admin@admin.com");
        notification.setReceiver(user2);
        notification.setStatus(reclamation.getStatus());
        notification.setMessage("A new reclamation has been created.");
        notification.setSendDate(Date.valueOf(reclamation.getReclamationDate()));
        notification.setReclamationId(createdReclamation.getId());
        notification.setSeen(false);
        notificationService.sendNotification(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        return new ResponseEntity<>(createdReclamation, HttpStatus.CREATED);
    }
}
