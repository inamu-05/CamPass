class CertificateHistory {
  final String purchaseDate;
  final String name;
  final int price;
  final int quantity;
  final String payment;
  final String receive;
  final String status;

  CertificateHistory({
    required this.purchaseDate,
    required this.name,
    required this.price,
    required this.quantity,
    required this.payment,
    required this.receive,
    required this.status,
  });

  factory CertificateHistory.fromJson(Map<String, dynamic> json) {
    return CertificateHistory(
      purchaseDate: json['purchaseDate'],
      name: json['name'],
      price: json['price'],
      quantity: json['quantity'],
      payment: json['payment'],
      receive: json['receive'],
      status: json['status'],
    );
  }
}
