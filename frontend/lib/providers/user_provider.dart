import 'package:flutter/material.dart';

class UserProvider with ChangeNotifier {
  // These variables hold the logged-in user's data
  String? _userId;
  String? _userName;
  String? _courseId;
  String? _email;
  String? _img; // Path to image

  // Getters to access the data safely
  String get userId => _userId ?? "";
  String get userName => _userName ?? "Guest";
  String get courseId => _courseId ?? "";
  String get email => _email ?? "";
  String get img => _img ?? "";

  // Check if user is logged in
  bool get isLoggedIn => _userId != null;

  // Method to set user data (Call this after Login)
  void setUser(Map<String, dynamic> data) {
    _userId = data['userId'];
    _userName = data['userName'];
    _courseId = data['courseId'];
    _email = data['email'];
    _img = data['img'];
    
    // Notify all screens that listen to this provider to update
    notifyListeners();
  }

  // Method to logout
  void logout() {
    _userId = null;
    _userName = null;
    _courseId = null;
    _email = null;
    _img = null;
    notifyListeners();
  }
}