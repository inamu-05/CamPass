import 'package:flutter/material.dart';
import '../main.dart'; // CustomAppBarを使用
import 'certificate_payment_screen.dart'; // 次の画面へ遷移
import '../services/certificate_service.dart'; // サービスクラスを読み込み
import '../services/auth_service.dart'; // AuthServiceをインポート

class CertificateApplicationScreen extends StatefulWidget {
  const CertificateApplicationScreen({Key? key}) : super(key: key);

  @override
  State<CertificateApplicationScreen> createState() =>
      _CertificateApplicationScreenState();
}

class _CertificateApplicationScreenState
    extends State<CertificateApplicationScreen> {
  List<Certificate> certificates = [];
  bool isLoading = true;
  String? errorMessage;

  @override
  void initState() {
    super.initState();
    _loadCertificates();
  }

  Future<void> _loadCertificates() async {
    try {
      String jwtToken = await AuthService.getJwtToken(); // ★ JWT取得
      final result = await CertificateService.fetchCertificates(jwtToken); // ★ JWT付きで呼び出す
      setState(() {
        certificates = result;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        errorMessage = "証明書情報の取得に失敗しました。";
        isLoading = false;
      });
      print('証明書取得エラー: $e');
    }
  }

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

            // ✅ ローディング・エラー・データ表示の切り替え
            Expanded(
              child: isLoading
                  ? const Center(child: CircularProgressIndicator())
                  : certificates.isEmpty
                      ? const Center(child: Text("証明書データがありません"))
                      : ListView.builder(
                          itemCount: certificates.length,
                          itemBuilder: (context, index) {
                            final cert = certificates[index];

                            print("証明書: ${cert.certificateId}, ${cert.certificateName}, ${cert.price}");

                            cert.quantity ??= 0; // quantityがnullなら0に

                            return Container(
                              margin: const EdgeInsets.only(bottom: 16),
                              padding: const EdgeInsets.all(16),
                              decoration: BoxDecoration(
                                border: Border.all(color: Colors.grey.shade400),
                                borderRadius: BorderRadius.circular(12),
                                color: Colors.white,
                              ),
                              child: Row(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceBetween,
                                children: [
                                  // 証明書名
                                  Expanded(
                                    flex: 2,
                                    child: Text(
                                      cert.certificateName,
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
                                      "￥${cert.price}",
                                      style: const TextStyle(fontSize: 18),
                                    ),
                                  ),

                                  // 枚数選択
                                  Expanded(
                                    flex: 1,
                                    child: DropdownButton<int>(
                                      value: cert.quantity,
                                      items: List.generate(
                                        11,
                                        (i) => DropdownMenuItem(
                                          value: i,
                                          child: Text(i.toString()),
                                        ),
                                      ),
                                      onChanged: (value) {
                                        setState(() {
                                          cert.quantity = value ?? 0;
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

            // ✅ エラーメッセージ表示
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
                      certificates.any((c) => (c.quantity ?? 0) > 0);

                  if (!hasSelection) {
                    setState(() {
                      errorMessage = "1つ以上の証明書を選択してください";
                    });
                    return;
                  }

                  setState(() {
                    errorMessage = null;
                  });

                  // ★選択した証明書のリストを作成（Payment画面へ渡す用）
                  final appliedCertificates = certificates
                      .where((c) => (c.quantity ?? 0) > 0)
                      .map((c) => {
                            "certificateId": c.certificateId,
                            "name": c.certificateName,
                            "price": c.price,
                            "quantity": c.quantity,
                          })
                      .toList();

                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => CertificatePaymentScreen(
                        appliedCertificates: appliedCertificates,  // ★ データを渡す
                      ),
                    ),
                  );
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor:
                      const Color.fromARGB(255, 147, 218, 231), // 水色
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
