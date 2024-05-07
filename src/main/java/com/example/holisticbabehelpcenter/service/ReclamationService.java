package com.example.holisticbabehelpcenter.service;

import com.example.holisticbabehelpcenter.model.Reclamation;
import com.example.holisticbabehelpcenter.repository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.holisticbabehelpcenter.model.User;
import java.util.List;

@Service
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;

    @Autowired
    public ReclamationService(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Reclamation createReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    public Reclamation getReclamationById(Long id) {
        return reclamationRepository.getById(id);
    }
    public List<Reclamation> getAllReclamationsByUser(User user) {
        return reclamationRepository.findByUser(user);
    }



}

