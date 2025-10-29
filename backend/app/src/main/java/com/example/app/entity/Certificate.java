package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "certificate")
public class Certificate {

	// 論理名称:証明書番号
	@Id
	@Column(name = "certificate_id", length = 2, nullable = false)
	private String staffCategory;

	// 論理名称:証明書名
	@Column(name = "certificate_name", nullable = false)
	private String certificateName;
	
	// 論理名称:金額
	@Column(name = "price", nullable = false)
	private Integer price;

	// 空のコンストラクタ
	public Certificate() {}

	// ゲッター・セッター
	public String getStaffCategory() {
		return staffCategory;
	}
	public void setStaffCategory(String staffCategory) {
		this.staffCategory = staffCategory;
	}
	public String getCertificateName() {
		return certificateName;
	}
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
}
