import 'package:flutter/material.dart';
import '../main.dart'; // 共通AppBarを利用

class AttendanceCompleteScreen extends StatelessWidget {
  final String otp; // ← 入力されたワンタイムパスワード（将来DB検索に使用）
  final String message; // <-- Add this field
  final String subjectName; // We will add this later
  final DateTime attendanceTime; // We will add this later

  const AttendanceCompleteScreen({
    Key? key, 
    required this.otp, 
    required this.message,
    required this.subjectName,
    required this.attendanceTime,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // ✅ 現在時刻を取得
    // final now = DateTime.now();
    final formattedTime =
        "${attendanceTime.year}年${attendanceTime.month}月${attendanceTime.day}日 ${attendanceTime.hour.toString().padLeft(2, '0')}:${attendanceTime.minute.toString().padLeft(2, '0')}:${attendanceTime.second.toString().padLeft(2, '0')}";

    // // ✅ 仮の授業名（将来的にMySQLから取得）
    // const className = "情報処理Ⅰ";
    // const message = "出席完了";

    return Scaffold(
      appBar: const CustomAppBar(title: "授業出席"),
      body: Padding(
        padding: const EdgeInsets.all(32.0),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              // ✅ 出席時刻
              Text(
                "出席時刻：\n$formattedTime",
                style: const TextStyle(
                  fontSize: 22,
                  fontWeight: FontWeight.w600,
                  color: Colors.black87,
                ),
              ),
              const SizedBox(height: 40),

              // ✅ 授業名を囲む枠付きカード
              Container(
                padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 40),
                decoration: BoxDecoration(
                  color: Colors.white,
                  border: Border.all(
                    color: const Color.fromARGB(255, 147, 218, 231),
                    width: 3,
                  ),
                  borderRadius: BorderRadius.circular(16),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.1),
                      blurRadius: 6,
                      offset: const Offset(0, 3),
                    ),
                  ],
                ),
                child: Column(
                  children: [
                    const Text(
                      "授業名",
                      style: TextStyle(
                        fontSize: 20,
                        color: Colors.black54,
                      ),
                    ),
                    const SizedBox(height: 10),
                    Text(
                      this.subjectName,
                      style: const TextStyle(
                        fontSize: 40,
                        fontWeight: FontWeight.bold,
                        color: Colors.black,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 40),

              // ✅ 出席完了メッセージ
              Text(
                this.message,
                textAlign: TextAlign.center,
                style: const TextStyle(
                  fontSize: 36,
                  fontWeight: FontWeight.bold,
                  color: Colors.green,
                ),
              ),
              const SizedBox(height: 60),

              // ✅ メインメニューへ戻るボタン
              SizedBox(
                width: double.infinity,
                height: 60,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.pushNamedAndRemoveUntil(
                        context, '/mainmenu', (route) => false);
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
    );
  }
}

 