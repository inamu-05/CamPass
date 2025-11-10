import 'package:flutter/material.dart';
import '../main.dart'; // 共通AppBarを利用

class StudentCardScreen extends StatelessWidget {
  const StudentCardScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: "学生証表示"),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24.0),
        child: Center(
          child: Column(
            children: [
              // ✅ 顔写真（枠組みなしで画像のみ表示）
              Image.asset(
                'assets/images/ina.jpg',  // ここで画像を指定
                fit: BoxFit.cover,  // 画像が枠に合わせて拡大・縮小される
                width: 140,
                height: 180,
              ),
              const SizedBox(height: 30),

              // ✅ 学生情報一覧
              _buildInfoRow("分　野", "情報処理"),
              const SizedBox(height: 8),
              _buildInfoRow("学　科", "情報システム"),
              const SizedBox(height: 8),
              _buildInfoRow("学生番号", "2401001"),
              const SizedBox(height: 8),
              _buildInfoRow("氏　名", "稲村 天良", bold: true),
              const SizedBox(height: 8),
              _buildInfoRow("生年月日", "2005年10月7日"),
              const SizedBox(height: 8),
              _buildInfoRow("発行日", "2024年4月1日"),
              const SizedBox(height: 8),
              _buildInfoRow("学生住所", "北海道札幌市南区石山2条2丁目1-30"),

              const SizedBox(height: 40),

              // ✅ 注意事項
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: const Color.fromARGB(255, 245, 253, 255),
                  border: Border.all(
                    color: const Color.fromARGB(255, 147, 218, 231),
                    width: 2,
                  ),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: const Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "【注意事項】",
                      style: TextStyle(
                        fontSize: 22,
                        fontWeight: FontWeight.bold,
                        color: Colors.black87,
                      ),
                    ),
                    SizedBox(height: 12),
                    Text(
                      "（１）この学生証は、常に携帯し請求があったときは呈示しなければならない。\n"
                      "（２）この学生証を紛失したときは、直ちに発行者に届け出なければならない。\n"
                      "（３）この学生証は、本校の学生でなくなったとき、又は有効期限が満了したときは、直ちに発行者に返さなければならない。",
                      style: TextStyle(fontSize: 16, color: Colors.black87, height: 1.6),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 40),

              // ✅ 学校名・印のセット（印部分に画像を挿入）
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text(
                    "道立工藤学園札幌校",
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(width: 20),
                  // 印部分に四角い画像をそのまま表示
                  Image.asset(
                    'assets/images/schoolstamp.png', // ここに印の画像を指定
                    width: 80,
                    height: 80,
                  ),
                ],
              ),

              const SizedBox(height: 60),
            ],
          ),
        ),
      ),
    );
  }

  // ✅ 情報行を整えるための共通ウィジェット
  Widget _buildInfoRow(String label, String value, {bool bold = false}) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          width: 110,
          child: Text(
            label,
            style: const TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
              color: Colors.black87,
            ),
          ),
        ),
        Expanded(
          child: Text(
            value,
            style: TextStyle(
              fontSize: 20,
              color: Colors.black87,
              fontWeight: bold ? FontWeight.bold : FontWeight.normal,
            ),
          ),
        ),
      ],
    );
  }
}
