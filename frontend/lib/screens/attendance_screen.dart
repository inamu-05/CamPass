import 'package:flutter/material.dart';
import '../main.dart'; // ← 共通AppBar利用時に必要
import 'attendance_complete_screen.dart';
 
class AttendanceScreen extends StatefulWidget {
  const AttendanceScreen({Key? key}) : super(key: key);
 
  @override
  State<AttendanceScreen> createState() => _AttendanceScreenState();
}
 
class _AttendanceScreenState extends State<AttendanceScreen> {
  final TextEditingController _otpController = TextEditingController();
  String? _errorMessage; // ✅ エラーメッセージを保持する変数
 
  void _handleSubmit() {
    String otp = _otpController.text.trim();
 
    setState(() {
      if (otp.isEmpty) {
        _errorMessage = 'ワンタイムパスワードを入力してください';
      } else if (otp.length != 4 || int.tryParse(otp) == null) {
        _errorMessage = '4桁の数字を入力してください';
      } else {
        _errorMessage = null;

      // ✅ 出席完了画面へ遷移
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => AttendanceCompleteScreen(otp: otp),
          ),
        );
      }
    });
  }
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: '授業出席'),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 40.0),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text(
                'ワンタイムパスワード',
                style: TextStyle(
                  fontSize: 26,
                  fontWeight: FontWeight.bold,
                  color: Colors.black87,
                ),
              ),
              const SizedBox(height: 20),
 
              // ✅ 入力欄
              SizedBox(
                width: 180,
                child: TextField(
                  controller: _otpController,
                  keyboardType: TextInputType.number,
                  maxLength: 4,
                  textAlign: TextAlign.center,
                  style: const TextStyle(fontSize: 28, letterSpacing: 8),
                  decoration: InputDecoration(
                    counterText: '',
                    filled: true,
                    fillColor: Colors.white,
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                ),
              ),
 
              const SizedBox(height: 10),
 
              // ✅ エラーメッセージ表示（赤文字）
              if (_errorMessage != null)
                Text(
                  _errorMessage!,
                  style: const TextStyle(color: Colors.red, fontSize: 16),
                ),
 
              const SizedBox(height: 30),
 
              // ✅ 出席ボタン
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: _handleSubmit,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color.fromARGB(255, 147, 218, 231),
                    padding: const EdgeInsets.symmetric(vertical: 16),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  child: const Text(
                    '出席',
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
 