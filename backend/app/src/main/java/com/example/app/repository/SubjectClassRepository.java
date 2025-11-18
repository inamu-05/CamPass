package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.SubjectClass;
import com.example.app.entity.SubjectClassId;

@Repository
public interface SubjectClassRepository extends JpaRepository<SubjectClass, SubjectClassId> {
    // List<SubjectClass> findBySubject(Subject subject);
    // List<Staff> findByStaff(Staff staff);
}