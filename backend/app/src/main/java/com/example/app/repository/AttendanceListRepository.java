package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Attendance;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceListRepository extends JpaRepository<Attendance, Long> {

        // 科目ID & 日付（LocalDateTime）範囲検索
        List<Attendance> findBySubject_SubjectIdAndId_SessionDatetimeBetween(
                String subjectId,
                LocalDateTime start,
                LocalDateTime end
        );

        // 科目IDだけで検索
        List<Attendance> findBySubject_SubjectId(String subjectId);

        // 日付だけ（LocalDateTime）範囲検索
        List<Attendance> findById_SessionDatetimeBetween(
                LocalDateTime start,
                LocalDateTime end
        );

         // ★ 授業単位で最新順に取得
        @Query("""
                SELECT a
                FROM Attendance a
                WHERE a.id.sessionDatetime IN (
                SELECT DISTINCT a2.id.sessionDatetime
                FROM Attendance a2
                )
                ORDER BY a.id.sessionDatetime DESC
        """)
        List<Attendance> findLatestLessons();

        // 科目ID & セッション日時で完全一致
        List<Attendance> findBySubject_SubjectIdAndId_SessionDatetime(
                String subjectId,
                LocalDateTime sessionDatetime
);
    
}
