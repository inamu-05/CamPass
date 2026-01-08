// File: lib/models/attendance_history_model.dart

class AttendanceHistory {
  final String name;      // 授業名
  final String date;      // 日時 (YYYY/MM/DD format)
  final String teacher;   // 教員名
  final String remarks;   // 備考 (e.g., "遅刻")

  AttendanceHistory({
    required this.name,
    required this.date,
    required this.teacher,
    required this.remarks,
  });

  factory AttendanceHistory.fromJson(Map<String, dynamic> json) {
    return AttendanceHistory(
      name: json['name'] as String,
      date: json['date'] as String,
      teacher: json['teacher'] as String,
      remarks: json['remarks'] as String,
    );
  }
}