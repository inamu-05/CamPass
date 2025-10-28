package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

	// 論理名称:ユーザ番号
	@Id
	@Column(name = "user_id", nullable = false)
	private String userId;

	// 論理名称:ユーザ氏名
	@Column(name = "user_name", nullable = false)
	private String userName;

	// 論理名称:所属学科
	@Column(name = "course_id", length = 2, nullable = false)
	private String courseId;

	// 論理名称:ユーザパスワード
	@Column(name = "user_pass", nullable = false)
	private String userPass;

	// 空のコンストラクタ
	public User() {}

	// ゲッター・セッター
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
}
