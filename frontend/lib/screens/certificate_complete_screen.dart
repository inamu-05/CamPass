import 'package:flutter/material.dart';
import '../main.dart'; // CustomAppBar用
import 'mainmenu_screen.dart'; // メインメニュー遷移用
 
class CertificateCompleteScreen extends StatelessWidget {
  const CertificateCompleteScreen({Key? key}) : super(key: key);
 
  @override
  Widget build(BuildContext context) {
    // 仮の申請データ（支払い画面で選択した内容を再現）
    final List<Map<String, dynamic>> appliedCertificates = [
      {"name": "卒業見込証明書", "price": 600, "quantity": 2},
      {"name": "成績証明書", "price": 600, "quantity": 1},
    ];
 
    // 合計金額の計算
    int total = appliedCertificates.fold(
      0,
      (sum, item) => sum + (item["price"] as int) * (item["quantity"] as int),
    );
 
    // 仮の選択内容
    const paymentMethod = "PayPay";
    const deliveryMethod = "窓口受取";
 
    return Scaffold(
      appBar: const CustomAppBar(title: "証明書申請"),
      body: Padding(
        padding: const EdgeInsets.all(32.0),
        child: Center(
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                // ✅ チェックアイコン
                const Icon(
                  Icons.check_circle_outline,
                  color: Colors.green,
                  size: 100,
                ),
                const SizedBox(height: 20),
 
                // ✅ 完了メッセージ
                const Text(
                  "申請完了",
                  style: TextStyle(
                    fontSize: 40,
                    fontWeight: FontWeight.bold,
                    color: Colors.black,
                  ),
                ),
                const SizedBox(height: 40),
 
                // ✅ 確認枠
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(20),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.grey.shade400),
                    borderRadius: BorderRadius.circular(12),
                    color: Colors.grey.shade50,
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        "確認内容",
                        style: TextStyle(
                          fontSize: 26,
                          fontWeight: FontWeight.bold,
                          color: Colors.black87,
                        ),
                      ),
                      const SizedBox(height: 16),
 
                      // ✅ 証明書一覧
                      Column(
                        children: appliedCertificates.map((cert) {
                          return Padding(
                            padding: const EdgeInsets.symmetric(vertical: 6.0),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Expanded(
                                  flex: 2,
                                  child: Text(
                                    cert["name"],
                                    style: const TextStyle(fontSize: 18),
                                  ),
                                ),
                                Expanded(
                                  child: Text(
                                    "￥${cert["price"]}",
                                    style: const TextStyle(fontSize: 18),
                                    textAlign: TextAlign.center,
                                  ),
                                ),
                                Expanded(
                                  child: Text(
                                    "${cert["quantity"]}枚",
                                    style: const TextStyle(fontSize: 18),
                                    textAlign: TextAlign.end,
                                  ),
                                ),
                              ],
                            ),
                          );
                        }).toList(),
                      ),
 
                      const SizedBox(height: 20),
 
                      // ✅ 合計金額
                      Align(
                        alignment: Alignment.centerRight,
                        child: Text(
                          "合計：￥$total",
                          style: const TextStyle(
                            fontSize: 22,
                            fontWeight: FontWeight.bold,
                            color: Colors.black87,
                          ),
                        ),
                      ),
 
                      const Divider(height: 40, thickness: 1),
 
                      // ✅ 支払い方法
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: const [
                          Text(
                            "お支払方法",
                            style: TextStyle(
                                fontSize: 20, fontWeight: FontWeight.bold),
                          ),
                          Text(
                            paymentMethod,
                            style: TextStyle(fontSize: 20),
                          ),
                        ],
                      ),
                      const SizedBox(height: 16),
 
                      // ✅ 受け取り方法
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: const [
                          Text(
                            "受け取り方法",
                            style: TextStyle(
                                fontSize: 20, fontWeight: FontWeight.bold),
                          ),
                          Text(
                            deliveryMethod,
                            style: TextStyle(fontSize: 20),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
 
                const SizedBox(height: 60),
 
                // ✅ メインメニューへ戻るボタン
                SizedBox(
                  width: double.infinity,
                  height: 60,
                  child: ElevatedButton(
                    onPressed: () {
                      Navigator.pushAndRemoveUntil(
                        context,
                        MaterialPageRoute(
                            builder: (context) => const MainMenuScreen()),
                        (route) => false,
                      );
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromARGB(255, 147, 218, 231),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    child: const Text(
                      "メインメニューへ戻る",
                      style: TextStyle(fontSize: 20, color: Colors.black),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}