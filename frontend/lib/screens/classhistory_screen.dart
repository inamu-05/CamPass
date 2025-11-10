import 'package:flutter/material.dart';
import '../main.dart';
 
class ClassHistoryScreen extends StatelessWidget {
  const ClassHistoryScreen({Key? key}) : super(key: key);
 
  @override
  Widget build(BuildContext context) {
    const studentNumber = '2440091';
    const studentName = '稲村天良';
 
    final classHistory = [
      {'name': 'AWS', 'date': '2025/10/30', 'teacher': '長尾晃佑', 'remarks': ''},
      {'name': 'オブジェクト指向', 'date': '2025/11/4', 'teacher': '長尾晃佑', 'remarks': ''},
      {'name': 'Python', 'date': '2025/11/5', 'teacher': '長尾晃佑', 'remarks': '遅刻'},
    ];
 
    classHistory.sort((a, b) => b['date']!.compareTo(a['date']!)); // 新しい順
 
    return Scaffold(
      appBar: const CustomAppBar(title: "授業履歴確認"),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: ListView(
          children: [
            const Text(
              "学生番号",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const Text("2401001", style: TextStyle(fontSize: 22)),
            const SizedBox(height: 20),
            const Text(
              "氏名",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const Text("稲村天良", style: TextStyle(fontSize: 22)),
            const SizedBox(height: 30),
 
            const Text(
              "授業履歴",
              style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
 
            ...classHistory.map((history) {
              final hasRemarks = history['remarks']!.isNotEmpty;
 
              return Container(
                margin: const EdgeInsets.symmetric(vertical: 8),
                padding:
                    const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
                decoration: BoxDecoration(
                  color: Colors.white, // 背景は白
                  borderRadius: BorderRadius.circular(12),
                  border: Border.all(
                    color: hasRemarks
                        ? Colors.orangeAccent
                        : Colors.grey.shade400, // 遅刻・欠席ならオレンジ枠
                    width: 1.2,
                  ),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      history['name']!,
                      style: const TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: Colors.black,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text("日時：${history['date']}"),
                    Text("教員：${history['teacher']}"),
                    if (hasRemarks) ...[
                      const SizedBox(height: 8),
                      Text(
                        "備考：${history['remarks']}",
                        style: const TextStyle(
                          color: Colors.orange,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ],
                  ],
                ),
              );
            }).toList(),
          ],
        ),
      ),
    );
  }
}