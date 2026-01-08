// File: lib/services/history_service.dart

import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/attendance_history_model.dart';
import 'auth_service.dart'; // Assuming you have an AuthService to get the token
import '../../main.dart';  // For API_BASE_URL

class HistoryService {
  final _apiBaseUrl = API_BASE_URL; // e.g., 'http://10.0.2.2:8080'

  Future<List<AttendanceHistory>> fetchClassHistory() async {
    final token = await AuthService.getJwtToken();
    if (token == null) {
      throw Exception('Authentication token not found.');
    }

    print("Token: "+token);

    final url = Uri.parse('$_apiBaseUrl/api/student/attendance-history');
    final response = await http.get(
      url,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    print("Response Status:"+response.statusCode.toString());

    if (response.statusCode == 200) {
      final String utf8Body = utf8.decode(response.bodyBytes);
      final List<dynamic> jsonList = jsonDecode(utf8Body);

      return jsonList.map((json) => AttendanceHistory.fromJson(json)).toList();
    } else {
      // Handle 401/403 or other errors appropriately
      throw Exception('授業履歴の取得に失敗しました (Status: ${response.statusCode})');
    }
  }
}