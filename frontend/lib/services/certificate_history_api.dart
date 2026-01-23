import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/certificate_history_response.dart';
import '../services/auth_service.dart';

const String baseUrl = 'http://10.0.2.2:8080';

Future<CertificateHistoryResponse> fetchCertificateHistory(
  int page, 
  int size
) async {
  final String jwtToken = await AuthService.getJwtToken();

  final response = await http.get(
    Uri.parse(
      '$baseUrl/api/cert/history/page?page=$page&size=$size',
    ),
    headers: {
      'Authorization': 'Bearer $jwtToken',
    },
  );

  if (response.statusCode == 200) {
    return CertificateHistoryResponse.fromJson(
      jsonDecode(utf8.decode(response.bodyBytes)),
    );
  } else {
    throw Exception('履歴取得に失敗しました');
  }
}
