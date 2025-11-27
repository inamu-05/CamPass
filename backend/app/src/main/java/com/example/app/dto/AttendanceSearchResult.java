package com.example.app.dto;

import com.example.app.entity.Attendance;
import java.util.List;
import java.util.Map;

/**
 * 出席リストの検索結果を保持するDTO
 * attendanceList: 重複削除済みの授業ごとのリスト
 * duplicateCounts: 授業ごとの出席人数 (キー: 科目ID_日時, 値: 人数)
 */
public class AttendanceSearchResult {
    
    private final List<Attendance> attendanceList;
    private final Map<String, Long> duplicateCounts;

    public AttendanceSearchResult(List<Attendance> attendanceList, Map<String, Long> duplicateCounts) {
        this.attendanceList = attendanceList;
        this.duplicateCounts = duplicateCounts;
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public Map<String, Long> getDuplicateCounts() {
        return duplicateCounts;
    }
}