import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/attendance_result.dart';

// IMPORTANT: Replace this with your computer's local IP address when testing on a real device or emulator.
// Use '10.0.2.2' if testing on an Android emulator or 'localhost' if running the Flutter web app on the same machine as Spring Boot.
const String _baseUrl = 'http://10.0.2.2:8080/api/validate-otp'; 

class AttendanceService {

  Future<AttendanceResult> recordAttendance({
    required String subjectId,
    required String pass,
    required String userId,
  }) async {
    final Map<String, dynamic> body = {
      'subjectId': subjectId,
      'pass': pass,
      'userId': userId,
    };

    try {
      print("----------------------------------------------------");
      // ... http post call ...
      // IMPORTANT: This assumes your Spring Boot API now returns JSON
      // { "message": "...", "subjectName": "...", "attendanceTime": "YYYY-MM-DDTHH:MM:SS" }
      final response = await http.post(
        Uri.parse(_baseUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(body),
      );

      if (response.statusCode == 200) {
        final String utf8Body = utf8.decode(response.bodyBytes);
        final Map<String, dynamic> data = jsonDecode(utf8Body);
        print("data: ${data}");

        return AttendanceResult(
          message: data['message'] ?? '出席が記録されました (Attendance Recorded Successfully!)',
          subjectName: data['subjectName'] ?? 'Unknown Subject',
          // Parse the ISO 8601 string from the backend
          attendanceTime: DateTime.parse(data['attendanceTime']),
        );
      } else {
        // Error handling remains similar, but now we'll throw an exception
        // so the calling screen can catch it.
        String errorMessage;
        if (response.statusCode == 401) {
          errorMessage = '認証失敗: パスワードが無効または期限切れです (Invalid or Expired Pass.)';
        } else if (response.statusCode == 500) {
            if (response.body.contains("Attendance record not found")) {
              errorMessage = 'エラー: 現在の授業の出席記録が見つかりません。教師に確認してください。(Record not pre-populated.)';
            } else {
              errorMessage = 'サーバーエラーが発生しました (Internal Server Error.)';
            }
        } else {
          errorMessage = 'エラーコード ${response.statusCode}: ${response.body}';
        }
        // Throw an error to be handled by the UI layer
        throw Exception(errorMessage);
      }
    } catch (e) {
      throw Exception('接続エラーが発生しました。インターネット接続を確認し、サーバーのIPアドレスを確認してください。(Connection Error: $e)');
    }
  }
}