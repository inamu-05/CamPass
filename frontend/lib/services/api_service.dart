import 'dart:convert';
import 'package:http/http.dart' as http;

class ApiService {
  final String baseUrl;

  ApiService({required this.baseUrl});

  /// Fetches the simple "Hello, Spring Boot!" greeting from the backend.
  Future<String> fetchHello() async {
    final url = Uri.parse('$baseUrl/hello');
    
    // Log URL to console for debugging
    final response = await http.get(url);

    String status = response.statusCode.toString();

    print('Attempting to connect to: $url, statusCode: $status'); 

    if (response.statusCode == 200) {
      // If the server returns plain text, return the body
      return response.body;
    } else {
      // Return error code for debugging purposes
      return 'HTTP Error: ${response.statusCode}'; 
    }
  }
}
