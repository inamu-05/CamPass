// File: lib/models/attendance_result.dart

class AttendanceResult {
  final String message;
  final String subjectName;
  final DateTime attendanceTime;

  AttendanceResult({
    required this.message,
    required this.subjectName,
    required this.attendanceTime,
  });
}