package com.example.holisticbabehelpcenter.repository;

import com.example.holisticbabehelpcenter.model.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
}
