package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.app.entity.Class;
// import java.util.List;

public interface ClassRepository extends JpaRepository<Class, String> {
    // List<Class> findByClassGroupId(String classGroupId);
}
