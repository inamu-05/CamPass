package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.entity.Attendance;
import com.example.app.entity.Student;
import com.example.app.repository.AttendanceListRepository;
import com.example.app.dto.AttendanceSearchResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceListService {

    @Autowired
    private AttendanceListRepository attendanceListRepository;

    // ===== 出席一覧検索 =====
    @Transactional(readOnly = true)
    public AttendanceSearchResult searchAttendance(String subjectId, LocalDate date) {

        System.out.println("==== Service:検索条件 ====");
        System.out.println("subjectId: " + subjectId);
        System.out.println("date: " + date);
        System.out.println("==================================");

        List<Attendance> result;

        // ===============================
        // 初期表示：最新授業5件
        // ===============================
        if ((subjectId == null || subjectId.isEmpty()) && date == null) {

            // 最新授業（学生分含む）を取得
            result = attendanceListRepository.findLatestLessons();

        } else {
            // ===============================
            // 検索あり
            // ===============================
            LocalDateTime start = null;
            LocalDateTime end = null;

            if (date != null) {
                start = date.atStartOfDay();
                end = date.plusDays(1).atStartOfDay().minusNanos(1);
            }

            if (subjectId != null && !subjectId.isEmpty() && date != null) {
                result = attendanceListRepository
                        .findBySubject_SubjectIdAndId_SessionDatetimeBetween(subjectId, start, end);
            } else if (subjectId != null && !subjectId.isEmpty()) {
                result = attendanceListRepository.findBySubject_SubjectId(subjectId);
            } else {
                result = attendanceListRepository.findById_SessionDatetimeBetween(start, end);
            }
        }

        // ===============================
        // 授業単位で1件にまとめる（表示用）
        // ===============================
        List<Attendance> distinctResult = result.stream()
            .collect(Collectors.toMap(
                a -> a.getSubject().getSubjectId() + "_" + a.getSessionDatetime(),
                a -> a,
                (existing, replacement) -> existing
            ))
            .values()
            .stream()
            .limit(5) // ★ 常に最大5授業
            .collect(Collectors.toList());

        // ===============================
        // 受講人数は DB から正しく数える
        // ===============================
        Map<String, Long> duplicateCounts = distinctResult.stream()
            .collect(Collectors.toMap(
                a -> a.getSubject().getSubjectId() + "_" + a.getSessionDatetime(),
                a -> attendanceListRepository
                        .findBySubject_SubjectIdAndId_SessionDatetime(
                            a.getSubject().getSubjectId(),
                            a.getSessionDatetime()
                        )
                        .stream()
                        .count()
            ));

        // ===============================
        // デバッグログ（確認用）
        // ===============================
        System.out.println("==== 授業ごとの出席学生一覧 ====");
        distinctResult.forEach(a -> {
            String key = a.getSubject().getSubjectId() + "_" + a.getSessionDatetime();
            System.out.println("授業キー: " + key);
            System.out.println("人数: " + duplicateCounts.get(key));

            attendanceListRepository
                .findBySubject_SubjectIdAndId_SessionDatetime(
                    a.getSubject().getSubjectId(),
                    a.getSessionDatetime()
                )
                .forEach(att -> {
                    Student s = att.getStudent();
                    System.out.println(
                        "  学生ID: " + (s != null ? s.getUserId() : "null")
                        + " / 学生名: " + (s != null ? s.getUserName() : "null")
                        + " / 出席: " + att.getIsAttended()
                    );
                });

            System.out.println("-----------------------------");
        });

        return new AttendanceSearchResult(distinctResult, duplicateCounts);
    }

    // ===== 詳細画面用 =====
    @Transactional(readOnly = true)
    public List<Attendance> findAttendanceDetails(String subjectId, LocalDateTime sessionDatetime) {

        List<Attendance> details =
            attendanceListRepository
                .findBySubject_SubjectIdAndId_SessionDatetime(subjectId, sessionDatetime);

        System.out.println("==== Service: 詳細データ取得結果 ====");
        System.out.println("授業キー: " + subjectId + "_" + sessionDatetime);
        System.out.println("学生レコード件数: " + details.size());
        System.out.println("=====================================");

        return details;
    }
}
