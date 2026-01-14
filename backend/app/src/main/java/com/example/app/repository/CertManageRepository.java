package com.example.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.CertManage;
// import java.util.List;
// import java.time.LocalDateTime;

/**
 * Repository interface for the Attendance entity, which uses a composite primary key.
 * * The generic parameters are:
 * 1. Attendance: The Entity class.
 * 2. AttendanceId: The class representing the composite primary key (@Embeddable).
 */
@Repository
public interface CertManageRepository extends JpaRepository<CertManage, Integer> {
    // 学生IDで全申請履歴を取得
    List<CertManage> findByStudent_UserId(String userId);
}
