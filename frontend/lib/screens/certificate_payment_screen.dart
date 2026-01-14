import 'package:flutter/material.dart';
import '../main.dart'; // CustomAppBar用
import 'certificate_complete_screen.dart'; // ← 確定後に遷移する完了画面（後で作成）
import '../services/cert_manage_service.dart';
import '../services/auth_service.dart';


const String dummyJwt = "DUMMY_JWT_FOR_DEBUG";

String paymentCode(String value) {
  switch (value) {
    case "学校支払": return "1";
    case "コンビニ支払": return "2";
    case "PayPay": return "3";
    default: return "0";
  }
}

String receiveCode(String value) {
  switch (value) {
    case "窓口受取": return "1";
    case "郵送": return "2";
    case "データ配布": return "3";
    default: return "0";
  }
}

class CertificatePaymentScreen extends StatefulWidget {
  final List<Map<String, dynamic>> appliedCertificates;

  const CertificatePaymentScreen({
    super.key,
    required this.appliedCertificates,
  });

  @override
  State<CertificatePaymentScreen> createState() =>
      _CertificatePaymentScreenState();
}
 
class _CertificatePaymentScreenState extends State<CertificatePaymentScreen> {
  // 仮の申請データ（数量が1枚以上の証明書のみ）
  late List<Map<String, dynamic>> appliedCertificates;

  @override
  void initState() {
    super.initState();
    appliedCertificates = widget.appliedCertificates;
  }

 
  // プルダウン用変数
  String? selectedPaymentMethod;
  String? selectedDeliveryMethod;
 
  @override
  Widget build(BuildContext context) {
    // 合計金額の計算
    int total = appliedCertificates.fold(
      0,
      (sum, item) => sum + (item["price"] as int) * (item["quantity"] as int),
    );
 
    return Scaffold(
      appBar: const CustomAppBar(title: "証明書申請"),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // ✅ 申請内容タイトル
              const Text(
                "申請内容",
                style: TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16),
 
              // ✅ 証明書一覧
              Container(
                decoration: BoxDecoration(
                  border: Border.all(color: Colors.grey.shade400),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Column(
                  children: appliedCertificates.map((cert) {
                    return Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 16),
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
 
              const SizedBox(height: 40),
 
              // ✅ 支払い方法
              const Text(
                "お支払方法",
                style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              DropdownButtonFormField<String>(
                value: selectedPaymentMethod,
                items: const [
                  DropdownMenuItem(value: "学校支払", child: Text("学校支払")),
                  DropdownMenuItem(value: "コンビニ支払", child: Text("コンビニ支払")),
                  DropdownMenuItem(value: "PayPay", child: Text("PayPay")),
                ],
                onChanged: (value) {
                  setState(() {
                    selectedPaymentMethod = value;
                  });
                },
                decoration: InputDecoration(
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(8)),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                ),
              ),
 
              const SizedBox(height: 30),
 
              // ✅ 受け取り方法
              const Text(
                "受け取り方法",
                style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              DropdownButtonFormField<String>(
                value: selectedDeliveryMethod,
                items: const [
                  DropdownMenuItem(value: "窓口受取", child: Text("窓口受取")),
                  DropdownMenuItem(value: "郵送", child: Text("郵送")),
                  DropdownMenuItem(value: "データ配布", child: Text("データ配布")),
                ],
                onChanged: (value) {
                  setState(() {
                    selectedDeliveryMethod = value;
                  });
                },
                decoration: InputDecoration(
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(8)),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                ),
              ),
 
              const SizedBox(height: 50),
 
              // ✅ 確定ボタン
              SizedBox(
                width: double.infinity,
                height: 60,
                child: ElevatedButton(
                  onPressed: (selectedPaymentMethod != null &&
                          selectedDeliveryMethod != null)
                      ? () async {

                        final jwtToken = await AuthService.getJwtToken();

                        print("JWT = $jwtToken"); // デバッグ用

                        for (final cert in appliedCertificates) {
                          await CertManageService.applyCertificate(
                            jwtToken: jwtToken,
                            certificateId: cert["certificateId"], // ← id を必ず持たせる
                            quantity: cert["quantity"],
                            payment: paymentCode(selectedPaymentMethod!),
                            receive: receiveCode(selectedDeliveryMethod!),
                          );
                        }

                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => CertificateCompleteScreen(
                                appliedCertificates: appliedCertificates,        // Payment画面から作ったリスト
                                paymentMethod: selectedPaymentMethod!,           // 選択された支払い方法
                                deliveryMethod: selectedDeliveryMethod!,         // 選択された受け取り方法
                              ),
                            ),
                          );
                        }
                      : null,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color.fromARGB(255, 147, 218, 231),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  child: const Text(
                    "確定",
                    style: TextStyle(fontSize: 22, color: Colors.black),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}