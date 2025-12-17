import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import '../main.dart'; // 共通AppBarを利用
import '../services/auth_service.dart';

class StudentCardScreen extends StatefulWidget {
    const StudentCardScreen({Key? key}) : super(key: key);

    @override
    _StudentCardScreenState createState() => _StudentCardScreenState();
}

class _StudentCardScreenState extends State<StudentCardScreen> {
    Map<String, dynamic>? studentData;
    bool isLoading = true;

    final String baseUrl ='http://10.0.2.2:8080';
    
    @override
    void initState() {
        super.initState();
        _loadStudentData();
    }

    Future<void> _loadStudentData() async {
        try {
            String token = await AuthService.getJwtToken();
            
            // APIリクエストのURIをbaseUrlを含んだ完全なURIに変更
            final uri = Uri.parse('$baseUrl/api/student-card');

            final response = await http.get(
                uri,
                headers: {'Authorization': 'Bearer $token'},
            );

            if (response.statusCode == 200) {
                final data = jsonDecode(utf8.decode(response.bodyBytes));
                
                setState(() {
                    studentData = data;
                    isLoading = false;
                });
            } else {
                throw Exception('API取得エラー: ${response.statusCode}');
            }
        } catch (e) {
            print('学生情報取得エラー: $e');
            setState(() {
                isLoading = false;
            });
        }
    }

    Widget _buildPlaceholderImage() {
        return Image.asset(
            'assets/images/ina.jpg',
            fit: BoxFit.cover,
            width: 140,
            height: 180,
        );
    }

    @override
    Widget build(BuildContext context) {
        // 画像の相対パスを取得
        final String imageUrl = '$baseUrl/api/student/image';

        return Scaffold(
            appBar: const CustomAppBar(title: "学生証表示"),
            body: isLoading
                ? const Center(child: CircularProgressIndicator())
                : SingleChildScrollView(
                    padding: const EdgeInsets.all(24.0),
                    child: Center(
                        child: Column(
                            children: [
                                FutureBuilder<String>(
                                  future:AuthService.getJwtToken(),
                                  builder: (context, snapshot) {
                                    if (snapshot.hasData && snapshot.data!.isNotEmpty) {
                                      return Image.network(
                                        imageUrl,
                                        fit: BoxFit.cover,
                                        width: 140,
                                        height: 180,
                                        // 認証トークンを送る
                                        headers: {
                                          'Authorization': 'Bearer ${snapshot.data}',
                                        },

                                        // エラー対応
                                        errorBuilder: (context, error, stackTrace) {
                                            print('画像読込エラー： $error. Full URL: $imageUrl'); // エラー時にもURLを出力
                                            return _buildPlaceholderImage();
                                        },
                                      );
                                    }
                                    return _buildPlaceholderImage();
                                  },
                                ),   
                                const SizedBox(height: 30),

                                // ✅ 学生情報一覧
                                _buildInfoRow("分　野", "情報処理"),
                                const SizedBox(height: 8),
                                _buildInfoRow("学　科", studentData?['courseName'] ?? ""),
                                const SizedBox(height: 8),
                                _buildInfoRow("学生番号", studentData?['userId'] ?? ""),
                                const SizedBox(height: 8),
                                _buildInfoRow("氏　名", studentData?['userName'] ?? "", bold: true),
                                const SizedBox(height: 8),
                                _buildInfoRow("生年月日", studentData?['birth'] ?? ""),
                                const SizedBox(height: 8),
                                _buildInfoRow("発行日", "2024年4月1日"),
                                const SizedBox(height: 8),
                                _buildInfoRow("学生住所", studentData?['address'] ?? ""),

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