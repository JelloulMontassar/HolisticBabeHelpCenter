package com.example.holisticbabehelpcenter.dto;
import com.example.holisticbabehelpcenter.model.User;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReclamationDTO {
    private Long id;
    private User userId;
    private LocalDate reclamationDate;
    private String status;
    private String result;
    private String description;
    private byte[] screenshot;

}

