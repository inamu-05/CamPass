import 'package:flutter/material.dart';
import 'package:provider/provider.dart'; // new
import '../providers/user_provider.dart'; // new
import 'logout_screen.dart';
import '../main.dart'; // CustomAppBar 用
import 'attendance_screen.dart';
import 'studentcard_screen.dart';
import 'classhistory_screen.dart'; // ✅ ← 授業履歴画面を追加インポート
import 'certificate_application_screen.dart'; 
import 'certificatehistory_screen.dart';
import 'test_screen.dart';
 
class MainMenuScreen extends StatelessWidget {
  const MainMenuScreen({Key? key}) : super(key: key);
 
  // ✅ 共通で使うメニュー項目リスト
  List<PopupMenuEntry<String>> _buildMenuItems() {
    return [
      _menuItem('授業履歴', Icons.history),
      _menuItem('証明書履歴', Icons.description_outlined),
    ];
  }
 
  // ✅ メニュー項目を生成する共通関数
  static PopupMenuItem<String> _menuItem(String label, IconData icon) {
    return PopupMenuItem<String>(
      value: label,
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 8),
        child: Row(
          children: [
            Icon(icon, size: 28, color: Colors.black54),
            const SizedBox(width: 12),
            Text(label, style: const TextStyle(fontSize: 30)),
          ],
        ),
      ),
    );
  }
 
  // ✅ 汎用ボタン（学生証表示・授業出席・証明書申請に使用）
  Widget _buildMainButton({
    required String label,
    required VoidCallback onPressed,
  }) {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          backgroundColor: const Color.fromARGB(255, 147, 218, 231),
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
        child: Text(label, style: const TextStyle(fontSize: 18)),
      ),
    );
  }
 
  @override
  Widget build(BuildContext context) {
    final user = Provider.of<UserProvider>(context);
    return Scaffold(
      appBar: CustomAppBar(
        title: "メインメニュー",
        showHomeButton: false,
        actions: [
          PopupMenuButton<String>(
            icon: const Icon(Icons.menu, size: 70, color: Colors.black),
            itemBuilder: (context) => _buildMenuItems(),
            onSelected: (value) {
              if (value == '授業履歴') {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const ClassHistoryScreen(),
                  ),
                );
              } else if (value == '証明書履歴') {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const CertificateHistoryScreen(),
                  ),
                );
              }
            },
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(32.0),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircleAvatar(
                radius: 50,
                backgroundImage: user.img.isNotEmpty 
                    ? NetworkImage(API_BASE_URL + user.img) // careful with localhost here
                    : null,
                child: user.img.isEmpty ? const Icon(Icons.person, size: 50) : null,
              ),
              const SizedBox(height: 20),
              
              // Display the Name!
              Text(
                "ようこそ, ${user.userName} さん", // "Welcome, [Name] san"
                style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
              ),
              
              const SizedBox(height: 10),
              Text(
                "学科 ID: ${user.courseId}",
                style: const TextStyle(fontSize: 16, color: Colors.grey),
              ),
              Text(
                "Email: ${user.email}",
                style: const TextStyle(fontSize: 16, color: Colors.grey),
              ),
              const SizedBox(height: 20),
              _buildMainButton(
                label: '学生証表示',
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const StudentCardScreen()),
                  );
                },
              ),
              const SizedBox(height: 20),
              _buildMainButton(
                label: '授業出席',
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const AttendanceScreen()),
                  );
                },
              ),
              const SizedBox(height: 20),
              _buildMainButton(
                label: '証明書申請',
                 onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const CertificateApplicationScreen()),
                  );
                },
              ),
              const SizedBox(height: 20),
              _buildMainButton(
                label: 'Testing',
                 onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const TestScreen()),
                  );
                },
              ),
              const SizedBox(height: 30),
              SizedBox(
                width: double.infinity,
                child: OutlinedButton(
                  onPressed: () {
                    Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(builder: (context) => const LogoutScreen()),
                    );
                  },
                  style: OutlinedButton.styleFrom(
                    side: const BorderSide(color: Color.fromARGB(255, 147, 218, 231)),
                    padding: const EdgeInsets.symmetric(vertical: 14),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  child: const Text(
                    'ログアウト',
                    style: TextStyle(fontSize: 16, color: Colors.black87),
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