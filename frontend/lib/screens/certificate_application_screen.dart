import 'package:flutter/material.dart';
import '../main.dart'; // CustomAppBarを使用
import 'certificate_payment_screen.dart'; // 次の画面へ遷移
 
class CertificateApplicationScreen extends StatefulWidget {
  const CertificateApplicationScreen({Key? key}) : super(key: key);
 
  @override
  State<CertificateApplicationScreen> createState() =>
      _CertificateApplicationScreenState();
}
 
class _CertificateApplicationScreenState
    extends State<CertificateApplicationScreen> {
  // ✅ 仮の証明書データ（将来はMySQLから取得）
  final List<Map<String, dynamic>> certificates = [
    {'name': '卒業見込証明書', 'price': 600, 'quantity': 0},
    {'name': '成績証明書', 'price': 600, 'quantity': 0},
    {'name': '在学証明書', 'price': 600, 'quantity': 0},
    {'name': '履歴書', 'price': 200, 'quantity': 0},
  ];
 
  // ✅ エラーメッセージ用変数
  String? errorMessage;
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: "証明書申請"),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "申請する証明書を選択してください",
              style: TextStyle(
                fontSize: 22,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 20),
 
            // ✅ 証明書リスト部分
            Expanded(
              child: ListView.builder(
                itemCount: certificates.length,
                itemBuilder: (context, index) {
                  final cert = certificates[index];
                  return Container(
                    margin: const EdgeInsets.only(bottom: 16),
                    padding: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.grey.shade400),
                      borderRadius: BorderRadius.circular(12),
                      color: Colors.white,
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        // 書名
                        Expanded(
                          flex: 2,
                          child: Text(
                            cert['name'],
                            style: const TextStyle(
                              fontSize: 18,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        ),
 
                        // 金額
                        Expanded(
                          flex: 1,
                          child: Text(
                            "￥${cert['price']}",
                            style: const TextStyle(fontSize: 18),
                          ),
                        ),
 
                        // 枚数選択
                        Expanded(
                          flex: 1,
                          child: DropdownButton<int>(
                            value: cert['quantity'],
                            items: List.generate(
                              11,
                              (i) => DropdownMenuItem(
                                value: i,
                                child: Text(i.toString()),
                              ),
                            ),
                            onChanged: (value) {
                              setState(() {
                                cert['quantity'] = value ?? 0;
                                // ✅ エラーメッセージをリセット
                                errorMessage = null;
                              });
                            },
                          ),
                        ),
                      ],
                    ),
                  );
                },
              ),
            ),
 
            const SizedBox(height: 10),
 
            // ✅ エラーメッセージ表示（赤文字）
            if (errorMessage != null)
              Padding(
                padding: const EdgeInsets.only(bottom: 10),
                child: Center(
                  child: Text(
                    errorMessage!,
                    textAlign: TextAlign.center,
                    style: const TextStyle(
                      color: Colors.red,
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
 
            const SizedBox(height: 10),
 
            // ✅ 申請ボタン
            SizedBox(
              width: double.infinity,
              height: 60,
              child: ElevatedButton(
                onPressed: () {
                  final hasSelection =
                      certificates.any((c) => c['quantity'] > 0);
 
                  if (!hasSelection) {
                    setState(() {
                      errorMessage = "1つ以上の証明書を選択してください";
                    });
                    return;
                  }
 
                  // ✅ エラーメッセージをクリアして次画面へ
                  setState(() {
                    errorMessage = null;
                  });
 
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const CertificatePaymentScreen(),
                    ),
                  );
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color.fromARGB(255, 147, 218, 231),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
                child: const Text(
                  "申請",
                  style: TextStyle(fontSize: 20, color: Colors.black),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}