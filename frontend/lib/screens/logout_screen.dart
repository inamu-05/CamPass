import 'package:flutter/material.dart';
import '../main.dart'; // ← CustomAppBarを使うために必要
 
class LogoutScreen extends StatelessWidget {
  const LogoutScreen({Key? key}) : super(key: key);
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // ✅ 共通ヘッダーに置き換え
      appBar: CustomAppBar(
        title: "ログアウト",
        showMenu: false,
        showHomeButton: false,
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(32.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(
                Icons.check_circle_outline,
                color: Color.fromARGB(255, 147, 218, 231),
                size: 80,
              ),
              const SizedBox(height: 20),

              const Text(
                "ログアウトしました",
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),

              const SizedBox(height: 16),
              const Text(
                "ご利用ありがとうございました。",
                style: TextStyle(fontSize: 16, color: Colors.black54),
              ),

              const SizedBox(height: 40),

              // ✅ ログイン画面へ戻るボタン
              SizedBox(
                width: double.infinity,
                height: 50,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color.fromARGB(255, 147, 218, 231),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8),
                    ),
                  ),
                  onPressed: () {
                    Navigator.pushNamedAndRemoveUntil(
                      context,
                      '/login',
                      (route) => false,
                    );
                  },
                  child: const Text(
                    "ログイン画面へ",
                    style: TextStyle(fontSize: 18, color: Colors.black),
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

 