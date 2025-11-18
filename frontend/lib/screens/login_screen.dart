import 'package:flutter/material.dart';
import 'package:provider/provider.dart'; // new
import '../providers/user_provider.dart'; // new
import 'dart:convert'; // for utf8 New
import '../main.dart';

// 1. Add these imports for HTTP and JSON
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io'; // To check platform
 
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

  // 2. Define your backend URL
  // This is a CRITICAL step. See "Part 3" below.
  // For Android Emulator, use 10.0.2.2 to access your computer's localhost
  // For iOS Simulator, you can use 'localhost'
  // For a real device, use your computer's local IP address (e.g., 192.168.1.5)
  
  String getBaseUrl() {
    if (Platform.isAndroid) {
      // Android emulator
      return 'http://10.0.2.2:8080'; 
    } else if (Platform.isIOS) {
      // iOS simulator
      return 'http://localhost:8080';
    } else {
      // Web or other
      return 'http://localhost:8080';
    }
    // !! NOTE: For a REAL device, you must use your PC's network IP
    // (e.g., 'http://192.168.1.10:8080')
  }
 
  void _login() async {
    setState(() {
      isLoading = true;
      errorMessage = null; // 前回のエラーを消す
    });
 
    final studentId = studentIdController.text.trim();
    final password = passwordController.text.trim();
 
    // --- Input Check ---
    if (!RegExp(r'^[a-zA-Z0-9]{4,7}$').hasMatch(studentId)) {
    }

    // --- AUTHENTICATION LOGIC ---
    try {
      final url = Uri.parse('${getBaseUrl()}/api/student/login');

      // Create the request body
      final body = jsonEncode({
        'userId': studentId,
        'password': password,
      });

      // Send the POST request
      final response = await http.post(
        url,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: body,
      ).timeout(const Duration(seconds: 10)); // Add a timeout

       print(response.statusCode);
       print(body);

      // Handle the response
      if (response.statusCode == 200) {
        // --- SUCCESS ---
        // The backend sent back the student data
        final studentData = jsonDecode(utf8.decode(response.bodyBytes));

        // 2. Save to Provider (accessing provider without listening)
        // 'listen: false' is important inside a function!
        Provider.of<UserProvider>(context, listen: false).setUser(studentData);
        
        setState(() {
          isLoading = false;
          errorMessage = null;
        });
        
        // TODO: Save the student data securely (e.g., using flutter_secure_storage)
        // For now, just navigate
        
        setState(() {
          isLoading = false;
          errorMessage = null;
        });
        Navigator.pushReplacementNamed(context, '/mainmenu');

      } else if (response.statusCode == 401) {
        // --- AUTHENTICATION FAILED ---
        setState(() {
          isLoading = false;
          errorMessage = "学生番号またはパスワードが間違っています。";
        });
      } else {
        // --- OTHER SERVER ERROR ---
        setState(() {
          isLoading = false;
          errorMessage = "サーバーエラーが発生しました (${response.statusCode})";
        });
      }

    } catch (e) {
      // --- NETWORK OR TIMEOUT ERROR ---
      setState(() {
        isLoading = false;
        errorMessage = "ネットワークに接続できません。";
        print(e); // For debugging
      });
    }
    // --- End of new logic ---
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