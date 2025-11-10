import 'package:flutter/material.dart';
import '../main.dart';
 
class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);
 
  @override
  State<LoginScreen> createState() => _LoginScreenState();
}
 
class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController studentIdController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
 
  bool isLoading = false;
  String? errorMessage; // ← ここにエラーメッセージを保持
 
  void _login() async {
    setState(() {
      isLoading = true;
      errorMessage = null; // 前回のエラーを消す
    });
 
    final studentId = studentIdController.text.trim();
    final password = passwordController.text.trim();
 
    // --- 入力チェック ---
    if (!RegExp(r'^\d{7}$').hasMatch(studentId)) {
      setState(() {
        isLoading = false;
        errorMessage = "学生番号は7桁の数字で入力してください。";
      });
      return;
    }
    if (password.isEmpty) {
      setState(() {
        isLoading = false;
        errorMessage = "パスワードを入力してください。";
      });
      return;
    }
 
    // --- 仮の認証処理（本来はAPI呼び出し） ---
    await Future.delayed(const Duration(seconds: 1));
 
    // ---仮の学生番号とパスワード
    if (studentId == "1234567" && password == "pass") {
      setState(() {
        isLoading = false;
        errorMessage = null;
      });
      Navigator.pushReplacementNamed(context, '/mainmenu');
      //ScaffoldMessenger.of(context).showSnackBar(
        //const SnackBar(content: Text("ログイン成功")),
      //);
    } else {
      setState(() {
        isLoading = false;
        errorMessage = "学生番号またはパスワードが間違っています。";
      });
    }
  }
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(
        title: "学生ログイン",
        showMenu: false,
        showHomeButton: false,
        ),
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(32.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // ===== イメージキャラクター画像 =====
              Center(
                child: Image.asset(
                  'assets/images/sora.png',
                  height: 180, // ← サイズはお好みで調整
                ),
              ),
              const SizedBox(height: 24),
              // ===== タイトル =====
              const Center(
                child: Text(
                  "ログイン",
                  style: TextStyle(
                    fontSize: 32,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              const SizedBox(height: 40),
 
              // ===== 学生番号 =====
              const Text(
                "学生番号",
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
              ),
              const SizedBox(height: 8),
              TextField(
                controller: studentIdController,
                keyboardType: TextInputType.number,
                decoration: InputDecoration(
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                  hintText: "例：1234567",
                ),
              ),
              const SizedBox(height: 24),
 
              // ===== パスワード =====
              const Text(
                "パスワード",
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
              ),
              const SizedBox(height: 8),
              TextField(
                controller: passwordController,
                obscureText: true,
                decoration: InputDecoration(
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                  hintText: "パスワードを入力",
                ),
              ),
 
              // ===== エラーメッセージ =====
              if (errorMessage != null) ...[
                const SizedBox(height: 16),
                Center(
                  child: Text(
                    errorMessage!,
                    style: const TextStyle(
                      color: Colors.red,
                      fontSize: 14,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
              ],
 
              const SizedBox(height: 32),
 
              // ===== ログインボタン =====
              SizedBox(
                width: double.infinity,
                height: 50,
                child: ElevatedButton(
                  onPressed: isLoading ? null : _login,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Color.fromARGB(255, 147, 218, 231),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8),
                    ),
                  ),
                  child: isLoading
                      ? const CircularProgressIndicator(color: Colors.black)
                      : const Text(
                          "ログイン",
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