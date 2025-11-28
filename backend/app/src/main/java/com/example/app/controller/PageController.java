package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.app.entity.Subject;
import com.example.app.service.SubjectService;
import com.example.app.entity.Attendance;
import com.example.app.service.AttendanceListService;
// ★ 追加: AttendanceSearchResult をインポート
import com.example.app.dto.AttendanceSearchResult;

@Controller
public class PageController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private AttendanceListService attendanceListService;

    // ワンタイムパスページ
    @GetMapping("/onetimepass")
    public String oneTimePass() {
        return "main/onetimepass";
    }

    @GetMapping("/student/attendance")
    public String showAttendancePage(
        Model model,
        @RequestParam(required = false) String subjectId,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date
    ) {
        System.out.println("==== Controller:検索条件 ====");
        System.out.println("subjectId: " + subjectId);
        System.out.println("date: " + date);
        System.out.println("================");

        List<Subject> subjects = subjectService.findAllSubjects();
        model.addAttribute("subjects", subjects);

        // ★ 変更点 1: Serviceの戻り値を DTO で受け取る
        AttendanceSearchResult result = attendanceListService.searchAttendance(subjectId, date);
        
        // ★ 変更点 2: 出席リストを DTO から取得して Model に追加
        model.addAttribute("attendances", result.getAttendanceList());
        
        // ★ 変更点 3: 授業ごとの出席人数マップを Model に追加
        model.addAttribute("duplicateCounts", result.getDuplicateCounts());
        
        System.out.println("TEST ME"+result.getDuplicateCounts());
        
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("date", date);

        System.out.println("==== Controller: Service から返ってきた attendances 件数 ====");
        // DTOからリストを取り出す
        List<Attendance> attendances = result.getAttendanceList(); 
        
        System.out.println("件数: " + attendances.size());
        attendances.forEach(a -> System.out.println("subject: " + a.getSubject().getSubjectName()
                    + " / 日付: " + a.getId().getSessionDatetime()));
        System.out.println("==================================");

    // ★★★ ログ追記箇所：duplicateCounts の内容を出力 ★★★
        System.out.println("==== Controller: duplicateCounts の内容 ====");
        result.getDuplicateCounts().forEach((key, count) -> {
            System.out.println("キー (科目ID_日時): " + key + ", 出席人数: " + count);
        });
        System.out.println("===========================================");
        // ★★★ ログ追記箇所：ここまで ★★★
        return "main/attendance_list";
    }

    // 出席詳細ページ
    @GetMapping("/attendance/detail")
public String showAttendanceDetailPage(
    Model model,
    @RequestParam("subjectId") String subjectId,
    @RequestParam("datetime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime datetime
) {
    List<Attendance> attendanceDetails =
        attendanceListService.findAttendanceDetails(subjectId, datetime);

    model.addAttribute("attendanceDetails", attendanceDetails);

    String subjectName = attendanceDetails.stream()
        .findFirst()
        .map(a -> a.getSubject().getSubjectName())
        .orElse("該当授業なし");
    model.addAttribute("subjectName", subjectName);

    // ★ 追加: 出席人数カウント
    long attendanceCount = attendanceDetails.stream()
        .filter(a -> Boolean.TRUE.equals(a.getIsAttended()))
        .count();
    model.addAttribute("attendanceCount", attendanceCount);

    return "main/attendance_detail";
}




    // // 証明書ページ
    // @GetMapping("/cert/list")
    // public String showPage() {
    //     return "main/certificate_list";
    // }
}