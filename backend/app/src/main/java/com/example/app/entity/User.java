package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

	// 論理名称:ユーザ番号
	@Id
	@Column(name = "user_id", nullable = false)
	private String userId;

	// 論理名称:ユーザ氏名
	@Column(name = "user_name", nullable = false)
	private String userName;

	// 論理名称:フリガナ
	@Column(name = "furigana", nullable = false)
	private String furigana;

	// 論理名称:所属学科
	@ManyToOne(fetch =  FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	// 論理名称:ユーザパスワード
	@Column(name = "user_pass", nullable = false)
	private String userPass;

	// 空のコンストラクタ
	public User() {}

    public User(String userId, String userName, String furigana, Course course, String userPass) {
        this.userId = userId;
        this.userName = userName;
		this.furigana = furigana;
        this.course = course;
        this.userPass = userPass;
    }

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
	public String getFurigana() {
		return furigana;
	}
	public void setFurigana(String furigana) {
		this.furigana = furigana;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
}
