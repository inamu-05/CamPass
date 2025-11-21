import 'package:flutter/material.dart';
import '../main.dart'; // Assuming CustomAppBar is defined here
import '../services/attendance_service.dart';

class TestScreen extends StatefulWidget {
  const TestScreen({Key? key}) : super(key: key);

  @override
  State<TestScreen> createState() => _TestScreenState();
}

class _TestScreenState extends State<TestScreen> {
  // Controllers for the three required inputs
  final TextEditingController _subjectIdController = TextEditingController(text: '01'); // Added input
  final TextEditingController _userIdController = TextEditingController(text: '2201001'); // Added input
  final TextEditingController _otpController = TextEditingController();

  String? _errorMessage;
  bool _isLoading = false;
  final AttendanceService _attendanceService = AttendanceService();

  Future<void> _handleSubmit() async {
    final String subjectId = _subjectIdController.text.trim();
    final String userId = _userIdController.text.trim();
    final String otp = _otpController.text.trim();

    // 1. Basic Client-Side Validation
    if (subjectId.isEmpty || userId.isEmpty || otp.isEmpty) {
      setState(() {
        _errorMessage = '全ての項目を入力してください';
      });
      return;
    }
    if (otp.length != 4 || int.tryParse(otp) == null) {
      setState(() {
        _errorMessage = 'パスワードは4桁の数字を入力してください';
      });
      return;
    }

    setState(() {
      _errorMessage = null;
      _isLoading = true; // Start loading
    });

    // 2. API Call
    final String resultMessage = await _attendanceService.recordAttendance(
      subjectId: subjectId,
      pass: otp,
      userId: userId,
    );

    setState(() {
      _isLoading = false; // Stop loading
      // 3. Check for successful message and display result
      if (resultMessage.startsWith('出席が記録されました')) {
        // SUCCESS: Display success message
        _errorMessage = resultMessage; 
      } else {
        // FAILURE: Display error message
        _errorMessage = resultMessage;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Assuming CustomAppBar exists in ../main.dart
      appBar: const CustomAppBar(title: '授業出席'), 
      body: Padding(
        padding: const EdgeInsets.all(30.0),
        child: Center(
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text(
                  '出席登録',
                  style: TextStyle(
                    fontSize: 26,
                    fontWeight: FontWeight.bold,
                    color: Colors.black87,
                  ),
                ),
                const SizedBox(height: 30),

                // --- NEW INPUTS ---

                // Subject ID Input
                _buildInput(
                  controller: _subjectIdController,
                  label: '科目ID (Subject ID)',
                  hint: '例: 01',
                  keyboardType: TextInputType.text,
                ),
                const SizedBox(height: 15),

                // User ID Input
                _buildInput(
                  controller: _userIdController,
                  label: '学生ID (User ID)',
                  hint: '例: S101',
                  keyboardType: TextInputType.text,
                ),
                const SizedBox(height: 15),

                // --- OTP INPUT (Modified) ---

                // OTP Input
                _buildInput(
                  controller: _otpController,
                  label: 'ワンタイムパスワード (OTP)',
                  hint: '4桁のパスワード',
                  maxLength: 4,
                  keyboardType: TextInputType.number,
                  isOtp: true,
                ),
                
                const SizedBox(height: 20),

                // Error / Status Message Display
                if (_errorMessage != null)
                  Text(
                    _errorMessage!,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      // Green for success, red for error
                      color: _errorMessage!.startsWith('出席が記録されました') ? Colors.green[700] : Colors.red,
                      fontSize: 16,
                      fontWeight: FontWeight.w500,
                    ),
                  ),

                const SizedBox(height: 30),

                // Button (with loading state)
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: _isLoading ? null : _handleSubmit,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: _isLoading ? Colors.grey : const Color.fromARGB(255, 147, 218, 231),
                      padding: const EdgeInsets.symmetric(vertical: 16),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    child: _isLoading
                        ? const SizedBox(
                            height: 24,
                            width: 24,
                            child: CircularProgressIndicator(
                              color: Colors.black,
                              strokeWidth: 3,
                            ),
                          )
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
      ),
    );
  }
  
  // Helper widget to build the input fields cleanly
  Widget _buildInput({
    required TextEditingController controller,
    required String label,
    required String hint,
    required TextInputType keyboardType,
    int? maxLength,
    bool isOtp = false,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          label,
          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
        ),
        const SizedBox(height: 8),
        TextField(
          controller: controller,
          keyboardType: keyboardType,
          maxLength: maxLength,
          textAlign: isOtp ? TextAlign.center : TextAlign.left,
          style: isOtp ? const TextStyle(fontSize: 28, letterSpacing: 8) : null,
          decoration: InputDecoration(
            hintText: hint,
            counterText: '',
            filled: true,
            fillColor: Colors.grey[100],
            contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide.none,
            ),
          ),
        ),
      ],
    );
  }
}