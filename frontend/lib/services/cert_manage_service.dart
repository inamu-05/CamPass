import 'dart:convert';
import 'package:http/http.dart' as http;

class CertManageService {
  static const String baseUrl = 'http://10.0.2.2:8080/api/certmanage';

  static Future<void> applyCertificate({
    required String jwtToken,
    required String certificateId,
    required int quantity,
    required String payment,
    required String receive,
  }) async {
    final response = await http.post(
      Uri.parse(baseUrl),
      headers: {
        'Authorization': 'Bearer $jwtToken',
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'certificateId': certificateId,
        'quantity': quantity,
        'payment': payment,
        'receive': receive,
      }),
    );

    //確認用ログ
    print('statusCode = ${response.statusCode}');
    print('responseBody = ${response.body}');

    if (response.statusCode != 200) {
      throw Exception('申請登録に失敗しました');
    }
  }
}
