package com.example.app.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student extends User {

	// 論理名称:生年月日
	@Column(name = "birth", nullable = false)
	private LocalDate birth;

	// 論理名称:電話番号
	@Column(name = "tel", length = 12, nullable = false)
	private String tel;

	// 論理名称:メールアドレス
	@Column(name = "mail", nullable = false)
	private String mail;

	// 論理名称:住所
	@Column(name = "address", nullable = false)
	private String address;

	// 論理名称:クラス
	@Column(name = "class_group_id", nullable = false)
	private String classGroupId;
    
	// 論理名称:証明写真
	@Column(name = "img", nullable = false)
	private String img;

	// 論理名称:在籍状況
	@Column(name = "enrollment_status",length = 1, nullable = false)
	private String enrollmentStatus;

	// 論理名称:入学年
	@Column(name = "entry_year",length = 4, nullable = false)
	private String entryYear;

	// 論理名称:卒業年
	@Column(name = "graduation_year",length = 4, nullable = true)
	private String graduationYear;
	
	// 論理名称:学生証無効化フラグ
	@Column(name = "is_disabled", nullable = false)
	private Boolean isDisabled = false; // FALSE:有効, TRUE:無効
	
	// 空のコンストラクタ
	public Student() {}

	public Student(String userId, String userName, String furigana, String courseId, 
                   String userPass, LocalDate birth, String tel, String mail, String address, String classGroupId,
                   String img, String enrollmentStatus, String entryYear, String graduationYear, 
                   Boolean isDisabled) {
        super(userId, userName, furigana, courseId, userPass);
        // Initializes Staff-specific field
        this.birth = birth;
        this.tel = tel;
        this.mail = mail;
        this.address = address;
        this.classGroupId = classGroupId;
        this.img = img;
        this.enrollmentStatus = enrollmentStatus;
        this.entryYear = entryYear;
        this.graduationYear = graduationYear;
        this.isDisabled = isDisabled;
    }

	// ゲッター・セッター
	public LocalDate getBirth() {
		return birth;
	}
	public void setBirth(LocalDate birth) {
		this.birth = birth;	
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;	
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
    public String getClassGroupId() {
		return classGroupId;	
	}
	public void setClassGroupId(String classGroupId) {
		this.classGroupId = classGroupId;
	}
	public String getAddress() {
		return address;	
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getImg() {
		return img;	
	}
	public void setImg(String img) {
		this.img = img;	
	}
	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}
	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}
	public String getEntryYear() {
		return entryYear;
	}
	public void setEntryYear(String entryYear) {
		this.entryYear = entryYear;
	}
	public String getGraduationYear() {
		return graduationYear;
	}
	public void setGraduationYear(String graduationYear) {
		this.graduationYear = graduationYear;
	}
	public Boolean getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
}