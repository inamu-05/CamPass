import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:student_management_system/providers/user_provider.dart';
import 'dart:convert';
import 'auth_service.dart'; // To get the stored token
import '../main.dart'; // For API_BASE_URL (assuming it's defined there)

class Subject {
  final String id;
  final String name;

  Subject({required this.id, required this.name});

  factory Subject.fromJson(Map<String, dynamic> json) {
    return Subject(
      id: json['subjectId'],
      name: json['subjectName'],
    );
  }
}

const String _baseUrl = 'http://10.0.2.2:8080/api/subjects/current-student';

class SubjectService {
  final UserProvider userProvider;

  SubjectService(this.userProvider);
  // Use the same baseUrl logic as in LoginScreen
  String getBaseUrl() {
    // ... copy the getBaseUrl logic from LoginScreen ...
    if (Platform.isAndroid) {
        return 'http://10.0.2.2:8080'; 
    } else if (Platform.isIOS) {
        return 'http://localhost:8080';
    } else {
        return 'http://localhost:8080';
    }
  }

  // Future<List<Subject>> fetchSubjectsForStudent() async {
  //   final token = await AuthService.getJwtToken(); // Get the stored token
  //   if (token == null) {
  //     throw Exception('Not logged in. Token not found.');
  //   }

  //   final url = Uri.parse('${getBaseUrl()}/api/subjects/by-student'); // Assumed student-specific endpoint
    
  //   // Fallback to the teacher endpoint if the student one isn't ready:
  //   // final url = Uri.parse('${getBaseUrl()}/api/subjects/by-teacher');

  //   try {
  //     final response = await http.get(
  //       url,
  //       headers: {
  //         'Content-Type': 'application/json; charset=UTF-8',
  //         // CRITICAL: Include the JWT for authentication
  //         'Authorization': 'Bearer $token', 
  //       },
  //     ).timeout(const Duration(seconds: 10));

  //     if (response.statusCode == 200) {
  //       final List<dynamic> jsonList = jsonDecode(utf8.decode(response.bodyBytes));
  //       return jsonList.map((json) => Subject(
  //         id: json['subjectId'], 
  //         name: json['subjectName']
  //       )).toList();
  //     } else if (response.statusCode == 401 || response.statusCode == 403) {
  //       // Token invalid or expired
  //       throw Exception('認証エラー: トークンが無効です。');
  //     } else {
  //       throw Exception('科目の取得に失敗しました: ${response.statusCode}');
  //     }
  //   } catch (e) {
  //     print('Error fetching subjects: $e');
  //     throw Exception('ネットワークエラーまたはサーバーエラー。');
  //   }
  // }
  

  Future<List<Subject>> fetchSubjects() async {
    final token = userProvider.token; // Get token from your provider
    print('Token being sent: $token');

    if (token == null) {
      throw Exception("User not authenticated.");
    }

    try {
      final response = await http.get(
        Uri.parse(_baseUrl),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token', // <-- PASS THE JWT TOKEN
        },
      );
      print(response.headers);
      print(token);
      print(response.statusCode);

      if (response.statusCode == 200) {
        // Success: Parse the list of subjects
        final List<dynamic> jsonList = jsonDecode(utf8.decode(response.bodyBytes));
        return jsonList.map((json) => Subject.fromJson(json)).toList();
      } else if (response.statusCode == 204) {
        // 204 No Content (No subjects found)
        return [];
      } else if (response.statusCode == 401 || response.statusCode == 403) {
        // Authentication/Authorization Failure
        throw Exception("認証失敗: セッションが無効です。再度ログインしてください。(Authentication Failed.)");
      } else {
        // Other error status
        throw Exception('科目の取得中にエラーが発生しました: ${response.statusCode}');
      }
    } catch (e) {
      if (kDebugMode) {
        print('Subject fetch error: $e');
      }
      throw Exception('接続エラーが発生しました。サーバーが実行されているか確認してください。($e)');
    }
  }
}