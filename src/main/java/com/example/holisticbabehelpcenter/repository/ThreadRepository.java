package com.example.holisticbabehelpcenter.repository;

import com.example.holisticbabehelpcenter.model.Threads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends JpaRepository<Threads, Long> {
}
