// lib/models/certificate.dart
class Certificate {
  final String certificateId;
  final String certificateName;
  final int price;
  int quantity; // Flutter側での選択枚数管理用
 
  Certificate({
    required this.certificateId,
    required this.certificateName,
    required this.price,
    this.quantity = 0,
  });
 
  factory Certificate.fromJson(Map<String, dynamic> json) {
    return Certificate(
      certificateId: json['certificateId'],
      certificateName: json['certificateName'],
      price: json['price'],
      quantity: 0, // API に quantity はないので初期化
    );
  }
}