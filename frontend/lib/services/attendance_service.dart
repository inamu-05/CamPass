import 'dart:convert';
import 'package:http/http.dart' as http;

// IMPORTANT: Replace this with your computer's local IP address when testing on a real device or emulator.
// Use '10.0.2.2' if testing on an Android emulator or 'localhost' if running the Flutter web app on the same machine as Spring Boot.
const String _baseUrl = 'http://10.0.2.2:8080/api/validate-otp'; 

class AttendanceService {

  Future<String> recordAttendance({
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
      final response = await http.post(
        Uri.parse(_baseUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(body),
      );

      if (response.statusCode == 200) {
        return '出席が記録されました (Attendance Recorded Successfully!)';
      } else if (response.statusCode == 401) {
        return '認証失敗: パスワードが無効または期限切れです (Invalid or Expired Pass.)';
      } else if (response.statusCode == 500) {
        // Handle server errors, checking for the specific "record not found" issue
        if (response.body.contains("Attendance record not found")) {
            return 'エラー: 現在の授業の出席記録が見つかりません。教師に確認してください。(Record not pre-populated.)';
        }
        return 'サーバーエラーが発生しました (Internal Server Error.)';
      } else {
        return 'エラーコード ${response.statusCode}: ${response.body}';
      }
    } catch (e) {
      return '接続エラーが発生しました。インターネット接続を確認し、サーバーのIPアドレスを確認してください。(Connection Error: $e)';
    }
  }
}