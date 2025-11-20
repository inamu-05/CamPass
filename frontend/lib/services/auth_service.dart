import 'package:shared_preferences/shared_preferences.dart';
 
class AuthService {
  static const String _jwtKey = "jwt_token";
 
  // JWT保存
  static Future<void> saveJwtToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_jwtKey, token);
  }
 
  // JWT取得
  static Future<String> getJwtToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_jwtKey) ?? '';
  }
 
  // JWT削除（ログアウト時）
  static Future<void> removeJwtToken() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_jwtKey);
  }
}