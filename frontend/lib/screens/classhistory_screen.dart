// File: ClassHistoryScreen.dart (Modified)

import 'package:flutter/material.dart';
import 'package:provider/provider.dart'; // Assuming you use a state management solution like Provider
import '../main.dart';
import '../models/attendance_history_model.dart';
import '../services/history_service.dart';
import '../providers/user_provider.dart'; // To get the Student ID and Name

class ClassHistoryScreen extends StatefulWidget {
  const ClassHistoryScreen({Key? key}) : super(key: key);

  @override
  State<ClassHistoryScreen> createState() => _ClassHistoryScreenState();
}

class _ClassHistoryScreenState extends State<ClassHistoryScreen> {
  late Future<List<AttendanceHistory>> _historyFuture;
  final HistoryService _historyService = HistoryService();

  @override
  void initState() {
    super.initState();
    // Start fetching data immediately
    _historyFuture = _historyService.fetchClassHistory();
  }

  @override
  Widget build(BuildContext context) {
    // Assuming you can get the current user details (ID and Name) from a provider/store
    final currentUser = Provider.of<UserProvider>(context); 
    final studentNumber = currentUser.userId;
    final studentName = currentUser.userName;

    return Scaffold(
      appBar: const CustomAppBar(title: "授業履歴確認"),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: ListView(
          children: [
            Text(
              "学生番号：${studentNumber ?? ''}",
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Text(
              "氏名：${studentName ?? ''}",
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 24),

            const Text(
              "授業履歴",
              style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),

            FutureBuilder<List<AttendanceHistory>>(
              future: _historyFuture,
              builder: (context, snapshot) {
                print("snapshot: ");
                print(snapshot);
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator());
                } else if (snapshot.hasError) {
                  return Center(
                    child: Text('データのロードに失敗しました: ${snapshot.error}'),
                  );
                } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                  return const Center(child: Text('履歴がありません'));
                }

                final classHistory = snapshot.data!;
                
                // The data is already sorted by the backend: ORDER BY session_datetime DESC

                return Column(
                  children: classHistory.map((history) {
                    print(history);
                    final hasRemarks = history.remarks.isNotEmpty && history.remarks != "出席"; // Check if it's not empty AND not "出席" (normal attendance)

                    return Container(
                      // margin: const EdgeInsets.only(bottom: 16),
                      // padding: const EdgeInsets.all(16),
                      margin: const EdgeInsets.symmetric(vertical: 8),
                      padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
                      width: double.infinity,
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(color: Colors.grey.shade300),
                        boxShadow: [
                            BoxShadow(
                              color: Colors.grey.withOpacity(0.2),
                              blurRadius: 4,
                              offset: const Offset(0, 3),
                            ),
                          ],
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            history.name,
                            style: const TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                              color: Colors.black,
                            ),
                          ),
                          const SizedBox(height: 8),
                          Text("日時：${history.date}"),
                          Text("教員：${history.teacher}"),
                          if (hasRemarks) ...[
                            const SizedBox(height: 8),
                            Text(
                              "備考：${history.remarks}",
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
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}