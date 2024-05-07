package com.example.holisticbabehelpcenter.repository;

import com.example.holisticbabehelpcenter.model.Reclamation;
import com.example.holisticbabehelpcenter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByResult(String notResolved);
    List<Reclamation> findByUser(User user);
    List<Reclamation> findByStatus(String status);
    List<Reclamation> findByStatusAndResult(String status, String result);

}
