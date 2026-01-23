import 'package:student_management_system/models/certificate_history.dart';

class CertificateHistoryResponse {
  final String studentId;
  final String studentName;
  final List<CertificateHistory> history;

  final int page;
  final int size;
  final int totalPages;
  final int totalElements;
  final bool hasNext;
  final bool hasPrevious;

  CertificateHistoryResponse({
    required this.studentId,
    required this.studentName,
    required this.history,
    required this.page,
    required this.size,
    required this.totalPages,
    required this.totalElements,
    required this.hasNext,
    required this.hasPrevious,
  });

  factory CertificateHistoryResponse.fromJson(Map<String, dynamic> json) {
    return CertificateHistoryResponse(
      studentId: json['studentId'],
      studentName: json['studentName'],
      history: (json['history'] as List)
          .map((e) => CertificateHistory.fromJson(e))
          .toList(),
      page: json['page'],
      size: json['size'],
      totalPages: json['totalPages'],
      totalElements: json['totalElements'],
      hasNext: json['hasNext'],
      hasPrevious: json['hasPrevious'],
    );
  }
}
