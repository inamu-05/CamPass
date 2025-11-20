import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/certificate.dart';

class Certificate {
  final String certificateId;
  final String certificateName;
  final int price;
  int quantity; // Flutter側での選択枚数管理用に追加

  Certificate({
    required this.certificateId,
    required this.certificateName,
    required this.price,
    this.quantity = 0, // 初期値は0
  });

  factory Certificate.fromJson(Map<String, dynamic> json) {
    return Certificate(
      certificateId: json['certificateId'],
      certificateName: json['certificateName'],
      price: json['price'],
      quantity: 0, // APIにquantityはないので0で初期化
    );
  }
}

class CertificateService {
  static const String baseUrl = 'http://10.0.2.2:8080/api/certificates';

  /// Flutter側で保持しているJWTを引数で受け取る
  static Future<List<Certificate>> fetchCertificates(String jwtToken) async {
    final response = await http.get(
      Uri.parse(baseUrl),
      headers: {
        'Authorization': 'Bearer $jwtToken', // ← JWTをここで送信
        'Content-Type': 'application/json',
      },
    );

    print("=== API Raw Response ===");
    print(response.body);  // ★ここで生のJSONを確認

    if (response.statusCode == 200) {
      final List<dynamic> body = json.decode(utf8.decode(response.bodyBytes));

      print("=== Decoded JSON ===");
      print(body); // ★デコード後も確認

      return body.map((json) => Certificate.fromJson(json)).toList();
    } else {
      throw Exception('証明書情報の取得に失敗しました: ${response.statusCode}');
    }
  }
}
 