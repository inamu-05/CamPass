import 'package:flutter/material.dart';
import '../main.dart'; // CustomAppBar を使用
import '../models/certificate_history.dart';
import '../models/certificate_history_response.dart';
import '../services/certificate_history_api.dart';
 
class CertificateHistoryScreen extends StatefulWidget {
  const CertificateHistoryScreen({Key? key}) : super(key: key);

  @override
  State<CertificateHistoryScreen> createState() =>
      _CertificateHistoryScreenState();
}

class _CertificateHistoryScreenState
    extends State<CertificateHistoryScreen> {

      int currentPage = 0;
      final int pageSize = 10;

      Future<CertificateHistoryResponse>? historyFuture;

      String? studentId;
      String? studentName;

  @override
  void initState() {
    super.initState();
    _loadPage();
  }

  void _loadPage() {
    setState(() {
      historyFuture = fetchCertificateHistory(currentPage, pageSize);
    });
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
              child: FutureBuilder<CertificateHistoryResponse>(
                future: historyFuture,
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const Center(child: CircularProgressIndicator());
                  }
                  if (snapshot.hasError) {
                    return const Center(child: Text('データ取得エラー'));
                  }

                  final response = snapshot.data!;
                  final historyData = response.history;

                  // ★ 学生情報を初回だけセット
                  if (studentId == null) {
                    WidgetsBinding.instance.addPostFrameCallback((_) {
                      setState(() {
                        studentId = response.studentId;
                        studentName = response.studentName;
                      });
                    });
                  }

                  if (historyData.isEmpty) {
                    return const Center(child: Text('証明書申請履歴はありません'));
                  }

                  return Column(
                    children: [
                      Expanded(
                        child: ListView.builder(
                          itemCount: historyData.length,
                          itemBuilder: (context, index) {
                            final record = historyData[index];
                            return _buildHistoryCard(record);
                          },
                        ),
                      ),

                      const SizedBox(height: 8),

                      // ===== ページネーションUI =====
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          ElevatedButton(
                            onPressed: response.hasPrevious
                                ? () {
                                    currentPage--;
                                    _loadPage();
                                  }
                                : null,
                            child: const Text('前へ'),
                          ),
                          Text(
                            '${response.page + 1} / ${response.totalPages}',
                            style: const TextStyle(fontSize: 16),
                          ),
                          ElevatedButton(
                            onPressed: response.hasNext
                                ? () {
                                    currentPage++;
                                    _loadPage();
                                  }
                                : null,
                            child: const Text('次へ'),
                          ),
                        ],
                      ),
                    ],
                  );
                  
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
  Widget _buildHistoryCard(CertificateHistory record) {
    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFFF9F9F9),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.grey.shade300),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            "購入日：${record.purchaseDate}",
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
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
  }
}