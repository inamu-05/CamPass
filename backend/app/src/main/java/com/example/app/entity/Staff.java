package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff extends User {

	// 論理名称:職員区分
	@Column(name = "staff_category", length = 1, nullable = false)
	private String staffCategory;
	
	// 空のコンストラクタ
	public Staff() {}

	// ゲッター・セッター
	public String getStaffCategory() {
		return staffCategory;
	}
	public void setStaffCategory(String staffCategory) {
		this.staffCategory = staffCategory;
	}
}
