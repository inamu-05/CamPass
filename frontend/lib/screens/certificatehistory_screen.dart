import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import '../main.dart'; // CustomAppBar を使用
import '../models/certificate_history.dart';
import '../services/auth_service.dart';


 
class CertificateHistoryScreen extends StatefulWidget {
  const CertificateHistoryScreen({Key? key}) : super(key: key);

  @override
  State<CertificateHistoryScreen> createState() =>
      _CertificateHistoryScreenState();
}

class _CertificateHistoryScreenState
    extends State<CertificateHistoryScreen> {

      final String baseUrl = 'http://10.0.2.2:8080';

      late Future<List<CertificateHistory>> historyFuture;

      String? studentId;
      String? studentName;

  @override
  void initState() {
    super.initState();
    historyFuture = fetchHistory();
  }

  Future<List<CertificateHistory>> fetchHistory() async {
    final token = await AuthService.getJwtToken(); // 既存JWT取得処理

    final response = await http.get(
      Uri.parse('$baseUrl/api/cert/history'),
      headers: {
        'Authorization': 'Bearer $token',
        //'Content-Type': 'application/json',
      },
    );

    // ★★★ デバッグ用 ★★★
    print('statusCode = ${response.statusCode}');
    print('raw body = ${utf8.decode(response.bodyBytes)}');

    if (response.statusCode == 200) {
      final body = json.decode(utf8.decode(response.bodyBytes));

      // ★ 学生情報を保存
      setState(() {
        studentId = body['studentId'];
        studentName = body['studentName'];
      });

      // ★ 履歴リストを取得
      final List historyList = body['history'];

      return historyList
          .map((e) => CertificateHistory.fromJson(e))
          .toList();

    } else {
      throw Exception('履歴取得に失敗しました');
    }
  }

 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: "証明書履歴"),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // ===== 学生情報 =====
            Text(
              "学生番号：${studentId ?? ''}",
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Text(
              "氏名：${studentName ?? ''}",
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),

            const SizedBox(height: 24),
 
            // ===== タイトル =====
            const Text(
              "証明書申請履歴",
              style: TextStyle(
                fontSize: 22,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 16),
 
            // ===== 履歴リスト =====
            Expanded(
              child: FutureBuilder<List<CertificateHistory>>(
                future: historyFuture,
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const Center(child: CircularProgressIndicator());
                  }
                  if (snapshot.hasError) {
                    return const Center(child: Text('データ取得エラー'));
                  }

                  final historyData = snapshot.data!;

                  if (historyData.isEmpty) {
                    return const Center(child: Text('証明書申請履歴はありません'));
                  }

                  return ListView.builder(
                    itemCount: historyData.length,
                    itemBuilder: (context, index) {
                      final record = historyData[index];

                      return Container(
                        margin: const EdgeInsets.only(bottom: 16),
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(
                          color: const Color(0xFFF9F9F9),
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(color: Colors.grey.shade300),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.grey.withOpacity(0.2),
                              blurRadius: 4,
                              offset: const Offset(0, 3),
                            ),
                          ],
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              "購入日：${record.purchaseDate}",
                              style: const TextStyle(
                                  fontSize: 16, fontWeight: FontWeight.bold),
                            ),
                            const SizedBox(height: 8),
                            Text("書名：${record.name}", style: const TextStyle(fontSize: 16)),
                            Text("金額：￥${record.price}", style: const TextStyle(fontSize: 16)),
                            Text("枚数：${record.quantity}枚", style: const TextStyle(fontSize: 16)),
                            Text("支払方法：${record.payment}", style: const TextStyle(fontSize: 16)),
                            Text("受取方法：${record.receive}", style: const TextStyle(fontSize: 16)),
                            const SizedBox(height: 4),
                            Text(
                              "申請状態：${record.status}",
                              style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: record.status == '申請中'
                                    ? Colors.orange
                                    : record.status == '支払済'
                                        ? Colors.blue
                                        : Colors.green,
                              ),
                            ),
                          ],
                        ),
                      );
                    },
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}