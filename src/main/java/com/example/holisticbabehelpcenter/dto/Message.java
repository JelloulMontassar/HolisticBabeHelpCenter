package com.example.holisticbabehelpcenter.dto;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    private Long id;
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private String status;
}
