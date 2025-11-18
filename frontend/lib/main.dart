import 'package:flutter/material.dart';
import 'package:provider/provider.dart'; // New
import 'providers/user_provider.dart';   // New
import 'package:flutter/foundation.dart' show kIsWeb;
import 'dart:io' show Platform;
import 'services/api_service.dart';
import 'screens/login_screen.dart';
import 'screens/logout_screen.dart';
import 'screens/mainmenu_screen.dart';
import 'screens/attendance_screen.dart';
import 'screens/studentcard_screen.dart';
import 'screens/classhistory_screen.dart';
import 'screens/certificate_application_screen.dart';
import 'screens/certificatehistory_screen.dart';
import 'screens/test_screen.dart';
 
// --- CRITICAL CONNECTION LOGIC ---
 
String getApiBaseUrl() {
  const String hostMachineIP = 'http://localhost:8080';
 
  if (kIsWeb) {
    return hostMachineIP;
  } else if (Platform.isAndroid) {
    return 'http://10.0.2.2:8080';
  } else if (Platform.isIOS) {
    return hostMachineIP;
  }
  return hostMachineIP;
}
 
final String API_BASE_URL = getApiBaseUrl();
 
// --- APP ENTRY POINT ---
 
void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => UserProvider()),
      ],
      child: const MyApp(),
    ),
  );
}
 
class CustomAppBar extends StatelessWidget implements PreferredSizeWidget {
  final String title;
  final List<Widget>? actions;
  final bool showMenu; // ✅ メニュー表示切り替え
  final bool showHomeButton; // ✅ ← 家アイコンを表示するかどうか（追加）
 
  const CustomAppBar({
    Key? key,
    required this.title,
    this.actions,
    this.showMenu = true,
    this.showHomeButton = true, // ✅ デフォルトは true（基本的に表示）
  }) : super(key: key);
 
  @override
  Size get preferredSize => const Size.fromHeight(120);
 
  @override
  Widget build(BuildContext context) {
    return AppBar(
      backgroundColor: const Color.fromARGB(255, 147, 218, 231),
      centerTitle: true,
      toolbarHeight: 120,
      automaticallyImplyLeading: false, // ✅ デフォルト戻るボタンを無効化
      title: Text(
        title,
        style: const TextStyle(
          fontSize: 34,
          fontWeight: FontWeight.bold,
          color: Colors.black,
        ),
      ),
 
      // ✅ 左上に「🏠家アイコン」を追加
      leading: showHomeButton
          ? IconButton(
              icon: const Icon(Icons.home, size: 40, color: Colors.black),
              onPressed: () {
                Navigator.pushReplacementNamed(context, '/mainmenu');
              },
            )
          : null,
 
      // ✅ showMenu=false の場合は非表示
      actions: showMenu
          ? (actions ??
              [
                PopupMenuButton<String>(
                  icon: const Icon(Icons.menu, size: 70, color: Colors.black),
                  itemBuilder: (BuildContext context) => [
                    _buildMenuItem(Icons.badge, '学生証'),
                    _buildMenuItem(Icons.check_circle_outline, '授業出席'),
                    _buildMenuItem(Icons.history, '授業履歴'),
                    _buildMenuItem(Icons.assignment, '証明書申請'),
                    _buildMenuItem(Icons.description_outlined, '証明書履歴'),
                    _buildMenuItem(Icons.description_outlined, 'Testing'),
                    const PopupMenuDivider(),
                    _buildMenuItem(Icons.logout, 'ログアウト'),
                  ],
                  onSelected: (String value) {
                    switch (value) {
                      case '学生証':
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const StudentCardScreen(),
                          ),
                        );
                        break;
 
                      case '授業出席':
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const AttendanceScreen(),
                          ),
                        );
                        break;

                        case 'Testing':
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const TestScreen(),
                          ),
                        );
                        break;
 
                      case '授業履歴':
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const ClassHistoryScreen(),
                          ),
                        );
                        break;
 
                      case '証明書申請':
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const CertificateApplicationScreen(),
                          ),
                        );
                        break;
 
                      case '証明書履歴':
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const CertificateHistoryScreen(),
                          ),
                        );
                        break;
 
                      case 'ログアウト':
                        Navigator.pushReplacement(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const LogoutScreen(),
                          ),
                        );
                        break;
                    }
                  },
                ),
              ])
          : null,
    );
  }
 
  // ✅ 共通メニュー項目生成用メソッド
  PopupMenuItem<String> _buildMenuItem(IconData icon, String label) {
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
}
 
 
 
 
class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);
 
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Student ID App',
      debugShowCheckedModeBanner: false,
 
      // ✅ 水色ベースの全体テーマ設定
      theme: ThemeData(
        scaffoldBackgroundColor: const Color.fromARGB(255, 245, 253, 255), // 全体背景（うっすい水色）
        appBarTheme: const AppBarTheme(
          backgroundColor: Color.fromARGB(255, 147, 218, 231), // 濃い水色
          foregroundColor: Colors.black87,
        ),
 
      textTheme: const TextTheme(
          bodyLarge: TextStyle(color: Colors.black87),
          bodyMedium: TextStyle(color: Colors.black87),
          bodySmall: TextStyle(color: Colors.black87),
          titleLarge: TextStyle(color: Colors.black87),
          titleMedium: TextStyle(color: Colors.black87),
          titleSmall: TextStyle(color: Colors.black87),
          labelLarge: TextStyle(color: Colors.black87),
          labelMedium: TextStyle(color: Colors.black87),
          labelSmall: TextStyle(color: Colors.black87),
        ),
 
        // フォーム要素（TextFieldなど）のテーマ
        inputDecorationTheme: InputDecorationTheme(
          filled: true,
          fillColor: Colors.white,
          enabledBorder: OutlineInputBorder(
            borderSide: const BorderSide(
              color: Color.fromARGB(255, 147, 218, 231), // 非フォーカス時の枠
              width: 1.5,
            ),
            borderRadius: BorderRadius.circular(10),
          ),
          focusedBorder: OutlineInputBorder(
            borderSide: const BorderSide(
              color: Color.fromARGB(255, 147, 218, 231), // フォーカス時の枠色（水色に変更）
              width: 2.5, // 少し太く（既存挙動を維持）
            ),
            borderRadius: BorderRadius.circular(10),
          ),
          labelStyle: const TextStyle(color: Colors.black87),
          hintStyle: const TextStyle(color: Colors.black45),
        ),
 
 
        // ボタンデザイン統一
        elevatedButtonTheme: ElevatedButtonThemeData(
          style: ElevatedButton.styleFrom(
            backgroundColor: const Color.fromARGB(255, 147, 218, 231),
            foregroundColor: Colors.black87,
            textStyle: const TextStyle(fontSize: 18),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(12)),
            ),
          ),
        ),
        outlinedButtonTheme: OutlinedButtonThemeData(
          style: OutlinedButton.styleFrom(
            side: const BorderSide(color: Color.fromARGB(255, 147, 218, 231)),
            foregroundColor: const Color.fromARGB(255, 147, 218, 231),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(12)),
            ),
          ),
        ),
 
        // ✅ ハンバーガーメニューの背景色も統一
        popupMenuTheme: const PopupMenuThemeData(
          color: Color.fromARGB(255, 245, 253, 255), // うっすい水色
          textStyle: TextStyle(color: Colors.black87),
        ),
      ),
 
      // ✅ ルーティング設定
      initialRoute: '/login',
      routes: {
        '/login': (context) => const LoginScreen(),
        '/logout': (context) => const LogoutScreen(),
        '/mainmenu': (context) => const MainMenuScreen(),
      },
    );
  }
}
 
// 以下はデモ用のメニュー類
 
class MainMenu extends StatelessWidget {
  const MainMenu({super.key});
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('メインメニュー')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            MenuButton(
              title: 'プロフィール',
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const ProfilePage()),
                );
              },
            ),
            MenuButton(
              title: '設定',
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const SettingsPage()),
                );
              },
            ),
            MenuButton(
              title: 'ヘルプ',
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const HelpPage()),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
 
class MenuButton extends StatelessWidget {
  final String title;
  final VoidCallback onTap;
 
  const MenuButton({super.key, required this.title, required this.onTap});
 
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10.0),
      child: ElevatedButton(
        onPressed: onTap,
        style: ElevatedButton.styleFrom(minimumSize: const Size(200, 50)),
        child: Text(title, style: const TextStyle(fontSize: 18)),
      ),
    );
  }
}
 
class ProfilePage extends StatelessWidget {
  const ProfilePage({super.key});
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('プロフィール')),
      body: const Center(child: Text('プロフィール画面です')),
    );
  }
}
 
class SettingsPage extends StatelessWidget {
  const SettingsPage({super.key});
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('設定')),
      body: const Center(child: Text('設定画面です')),
    );
  }
}
 
class HelpPage extends StatelessWidget {
  const HelpPage({super.key});
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('ヘルプ')),
      body: const Center(child: Text('ヘルプ画面です')),
    );
  }
}
 