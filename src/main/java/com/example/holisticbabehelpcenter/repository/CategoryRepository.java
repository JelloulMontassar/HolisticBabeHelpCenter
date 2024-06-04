package com.example.holisticbabehelpcenter.repository;

import com.example.holisticbabehelpcenter.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}

