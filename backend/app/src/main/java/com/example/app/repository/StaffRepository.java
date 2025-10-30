package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    // List<Staff> findByStaffName(String name);
    Staff findByUserId(String userId);
}