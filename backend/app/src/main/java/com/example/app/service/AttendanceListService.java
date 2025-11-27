package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.entity.Attendance;
import com.example.app.repository.AttendanceListRepository;
import com.example.app.dto.AttendanceSearchResult; // ★ 追加

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map; // ★ 追加
import java.util.stream.Collectors; // ★ 追加

@Service
public class AttendanceListService {

    @Autowired
    private AttendanceListRepository attendanceListRepository;

    // 戻り値の型を AttendanceSearchResult に変更
    public AttendanceSearchResult searchAttendance(String subjectId, LocalDate date) { 

        System.out.println("==== Service:検索条件 ====");
        System.out.println("subjectId: " + subjectId);
        System.out.println("date: " + date);
        System.out.println("==================================");

        List<Attendance> result;

        if ((subjectId == null || subjectId.isEmpty()) && date == null) {
            // 検索条件なし → 最新5件取得
            result = attendanceListRepository.findTop5ByOrderById_SessionDatetimeDesc();

        } else {
            LocalDateTime start = null;
            LocalDateTime end = null;

            if (date != null) {
                start = date.atStartOfDay();                    // 00:00
                end = date.plusDays(1).atStartOfDay().minusNanos(1); // 23:59:59.999999999
            }

            if (subjectId != null && !subjectId.isEmpty() && date != null) {
                // 科目ID + 日付範囲
                result = attendanceListRepository
                        .findBySubject_SubjectIdAndId_SessionDatetimeBetween(subjectId, start, end);

            } else if (subjectId != null && !subjectId.isEmpty()) {
                // 科目のみ
                result = attendanceListRepository.findBySubject_SubjectId(subjectId);

            } else {
                // 日付のみ
                result = attendanceListRepository.findById_SessionDatetimeBetween(start, end);
            }
        }

        System.out.println("==== Repositoryからの結果 (重複削除前) ====");
        result.forEach(System.out::println);
        System.out.println("=========================================");

        // ★ 授業単位で重複を件数表示 (出席人数をカウント)
        Map<String, Long> duplicateCounts = result.stream()
            .collect(Collectors.groupingBy(
                a -> a.getSubject().getSubjectId() + "_" + a.getId().getSessionDatetime(), // キー：科目＋日時
                Collectors.counting() // 各グループの件数をカウント
            ));

        System.out.println("==== 授業ごとの出席人数 (重複件数) ====");
        duplicateCounts.forEach((key, count) -> {
            System.out.println("キー: " + key + " - 出席人数: " + count + "人");
        });
        System.out.println("=========================================");

        // ★ 授業単位で重複削除（subjectId + sessionDatetime のセットで1件のみ）
        List<Attendance> distinctResult = result.stream()
                .collect(Collectors.toMap(
                        a -> a.getSubject().getSubjectId() + "_" + a.getId().getSessionDatetime(), // キー：科目＋日時
                        a -> a,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .toList();

        // ★ DTOに格納して返す
        return new AttendanceSearchResult(distinctResult, duplicateCounts);
    }

    public List<Attendance> findAttendanceDetails(String subjectId, LocalDateTime sessionDatetime) {
    
    // Repositoryに、科目IDとセッション日時で一致するすべてのAttendanceレコードを検索させる
    // ※ Repositoryに findBySubject_SubjectIdAndId_SessionDatetime が必要です
    List<Attendance> details = attendanceListRepository
        .findBySubject_SubjectIdAndId_SessionDatetime(subjectId, sessionDatetime);

    System.out.println("==== Service: 詳細データ取得結果 ====");
    System.out.println("授業キー: " + subjectId + "_" + sessionDatetime);
    System.out.println("学生レコード件数: " + details.size());
    System.out.println("=====================================");

    return details;
    }
}