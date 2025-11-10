import 'package:flutter/material.dart';
import '../main.dart'; // CustomAppBar を使用
 
class CertificateHistoryScreen extends StatelessWidget {
  const CertificateHistoryScreen({Key? key}) : super(key: key);
 
  @override
  Widget build(BuildContext context) {
    // 仮データ（新しい順）
    final List<Map<String, dynamic>> historyData = [
      {
        'purchaseDate': '2025/11/06',
        'name': '卒業見込証明書',
        'price': 600,
        'quantity': 2,
        'payment': 'PayPay',
        'receive': '窓口受取',
        'status': '受取済'
      },
      {
        'purchaseDate': '2025/11/01',
        'name': '成績証明書',
        'price': 600,
        'quantity': 1,
        'payment': '学校支払',
        'receive': 'データ配布',
        'status': '支払済'
      },
      {
        'purchaseDate': '2025/10/25',
        'name': '在学証明書',
        'price': 600,
        'quantity': 1,
        'payment': 'コンビニ支払',
        'receive': '郵送',
        'status': '申請中'
      },
    ];
 
    return Scaffold(
      appBar: const CustomAppBar(title: "証明書履歴"),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // ===== 学生情報 =====
            const Text(
              "学生番号：2401001",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            const Text(
              "氏名：稲村天良",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 20),
 
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
              child: ListView.builder(
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
                          "購入日：${record['purchaseDate']}",
                          style: const TextStyle(
                              fontSize: 16, fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 8),
                        Text("書名：${record['name']}", style: const TextStyle(fontSize: 16)),
                        Text("金額：￥${record['price']}", style: const TextStyle(fontSize: 16)),
                        Text("枚数：${record['quantity']}枚", style: const TextStyle(fontSize: 16)),
                        Text("支払方法：${record['payment']}", style: const TextStyle(fontSize: 16)),
                        Text("受取方法：${record['receive']}", style: const TextStyle(fontSize: 16)),
                        const SizedBox(height: 4),
                        Text(
                          "申請状態：${record['status']}",
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                            color: record['status'] == '申請中'
                                ? Colors.orange
                                : record['status'] == '支払済'
                                    ? Colors.blue
                                    : Colors.green,
                          ),
                        ),
                      ],
                    ),
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