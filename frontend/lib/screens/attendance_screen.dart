import 'package:flutter/material.dart';
import '../main.dart'; // ← 共通AppBar利用時に必要
import 'attendance_complete_screen.dart';
import 'package:provider/provider.dart';
import '../providers/user_provider.dart';
import '../services/subject_service.dart'; // 1. New import for SubjectService
import '../services/attendance_service.dart'; // 2. New import for AttendanceService (assuming you have one)
import 'dart:async'; // For FutureBuilder
 
class AttendanceScreen extends StatefulWidget {
  const AttendanceScreen({Key? key}) : super(key: key);
 
  @override
  State<AttendanceScreen> createState() => _AttendanceScreenState();
}
 
class _AttendanceScreenState extends State<AttendanceScreen> {
  final TextEditingController _otpController = TextEditingController();
  String? _errorMessage; // ✅ エラーメッセージを保持する変数
  String? _selectedSubjectId; // 3. State variable for selected subject ID
  List<Subject> _subjects = []; // 4. State variable for subject list
  bool _isLoadingSubjects = true; // 5. Loading state for subjects
  bool _isSubmitting = false; // 6. Loading state for attendance submi
  late SubjectService _subjectService;
  final AttendanceService _attendanceService = AttendanceService(); // Assuming you've built this service for API call
 
  // bool _isServiceInitialized = false;

  @override
    void initState() {
      super.initState();
      // _fetchSubjects();
      WidgetsBinding.instance.addPostFrameCallback((_) {
      _initializeServiceAndFetchSubjects();
      });
    }

  // @override
  // void didChangeDependencies() {
  //   super.didChangeDependencies();
  //   // Initialize the service using the context
  //   if (!_isServiceInitialized) {
  //   final userProvider = Provider.of<UserProvider>(context, listen: false);
  //   // if (!context.read<UserProvider>().isSame(Provider.of<UserProvider>(context, listen: false))) {
  //   //     // This check is overly complex. The simplest fix is to only initialize ONCE 
  //   //     // if it hasn't been set yet. However, since the class field is `late`, 
  //   //     // we'll use a boolean or simply initialize it, assuming the provider won't change.
  //   // }
  //   _subjectService = SubjectService(userProvider);
  //   _isServiceInitialized = true;
  //   // if (_subjects.isEmpty && _isLoadingSubjects) {
  //     _fetchSubjects();
  //   }
  // }

  // New method to handle initialization
  void _initializeServiceAndFetchSubjects() {
      // Access context safely
      final userProvider = Provider.of<UserProvider>(context, listen: false);
      
      // Initialize the late variable
      _subjectService = SubjectService(userProvider);
      
      // Check if subjects are empty before fetching
      if (_subjects.isEmpty) {
        _fetchSubjects();
      }
  }

  // 7. Method to fetch subjects from the backend
  Future<void> _fetchSubjects() async {
    setState(() {
       _isLoadingSubjects = true; // Start subject loading
       _errorMessage = null; // Clear previous errors
    });
    try {
      final fetchedSubjects = await _subjectService.fetchSubjects();
      setState(() {
        _subjects = fetchedSubjects;
        _selectedSubjectId = _subjects.isNotEmpty ? _subjects.first.id : null;
      });
    } catch (e) {
      // Handle error (e.g., display a dialog)
      print('Error fetching subjects: $e');
      setState(() {
        // You might want to store a specific error message here
        // and display it instead of the "no subjects" message.
        _errorMessage = '科目リストの取得に失敗しました。認証を確認してください。';
      });
    } finally {
      setState(() {
        _isLoadingSubjects = false; // Stop subject loading
      });
    }
  }

  void _handleSubmit() async {
    if (_selectedSubjectId == null) {
      setState(() => _errorMessage = '科目を選択してください');
      return;
    }

    final String otp = _otpController.text.trim();
    final userProvider = Provider.of<UserProvider>(context, listen: false);
    final String studentId = userProvider.userId; // Get logged-in student ID
    
    // --- Input Check ---
    if (otp.isEmpty) {
      setState(() => _errorMessage = 'ワンタイムパスワードを入力してください');
      return;
    } else if (otp.length != 4 || int.tryParse(otp) == null) {
      setState(() => _errorMessage = '4桁の数字を入力してください');
      return;
    }
    
    setState(() {
      _errorMessage = null;
      _isSubmitting = true; // Start submission loading
    });

    // --- API CALL FOR ATTENDANCE ---
    try {
        // You'll need to create an AttendanceService and a method like this
        // to send the request to your Spring Boot attendance endpoint
        final result = await _attendanceService.recordAttendance(
            subjectId: _selectedSubjectId!,
            pass: otp,
            userId: studentId,
        );
        
        // Success: Navigate to complete screen
        Navigator.pushReplacement(
            context,
            MaterialPageRoute(
                builder: (context) => AttendanceCompleteScreen(
                    otp: otp,
                    message: result.message, // Pass the message from the result object
                    subjectName: result.subjectName, // Pass the subject name
                    attendanceTime: result.attendanceTime, // Pass the attendance time
                ),
            ),
        );
    } catch (e) {
        // Failure: Show error
        setState(() {
          String errorStr = e.toString();
          if (errorStr.contains('401')) {
            _errorMessage = '認証に失敗しました。再度ログインしてください。';
          } else if (errorStr.contains('403')) {
            _errorMessage = '出席期間外です。';
          } else if (errorStr.contains('SocketException')) {
            _errorMessage = 'ネットワーク接続を確認してください。';
          } else {
            _errorMessage = '科目かワンタイムパスワードが正しくありません。';
          }
        });
    } finally {
        setState(() {
            _isSubmitting = false; // Stop submission loading
        });
    }
  }

  // 8. Widget to build the Subject Dropdown
  Widget _buildSubjectDropdown() {
    if (_isLoadingSubjects) {
      return const Center(child: CircularProgressIndicator());
    }
    if (_subjects.isEmpty) {
      return const Center(
        child: Text('現在出席可能な科目はありません。', style: TextStyle(color: Colors.red)),
      );
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          "科目を選択", // Select Subject
          style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
        ),
        const SizedBox(height: 8),
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 16.0),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(12),
            border: Border.all(color: Colors.grey.shade300),
          ),
          child: DropdownButtonHideUnderline(
            child: DropdownButton<String>(
              value: _selectedSubjectId,
              isExpanded: true,
              icon: const Icon(Icons.arrow_drop_down),
              elevation: 16,
              style: const TextStyle(fontSize: 18, color: Colors.black87),
              onChanged: (String? newValue) {
                setState(() {
                  _selectedSubjectId = newValue;
                  _errorMessage = null; // Clear error on change
                });
              },
              items: _subjects.map<DropdownMenuItem<String>>((Subject subject) {
                return DropdownMenuItem<String>(
                  value: subject.id, // Value is the ID
                  child: Text(subject.name), // Display is the Name
                );
              }).toList(),
            ),
          ),
        ),
        const SizedBox(height: 20),
      ],
    );
  }

  // void _handleSubmit() {
  //   String otp = _otpController.text.trim();
 
  //   setState(() {
  //     if (otp.isEmpty) {
  //       _errorMessage = 'ワンタイムパスワードを入力してください';
  //     } else if (otp.length != 4 || int.tryParse(otp) == null) {
  //       _errorMessage = '4桁の数字を入力してください';
  //     } else {
  //       _errorMessage = null;

  //     // ✅ 出席完了画面へ遷移
  //       Navigator.push(
  //         context,
  //         MaterialPageRoute(
  //           builder: (context) => AttendanceCompleteScreen(otp: otp),
  //         ),
  //       );
  //     }
  //   });
  // }
 
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
              // 9. Insert the Dropdown Widget here
              _buildSubjectDropdown(),

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
                  child: _isSubmitting
                        ? const CircularProgressIndicator(color: Colors.black)
                        : const Text(
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
 