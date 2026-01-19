package com.example.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.app.entity.ClassGroup;
// import java.util.List;

public interface ClassGroupRepository extends JpaRepository<ClassGroup, String> {
    // List<Class> findByClassGroupId(String classGroupId);

    Optional<ClassGroup> findByClassGroup(String classGroup);
}
