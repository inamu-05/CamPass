package com.example.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "subject")
public class Subject {

	// 論理名称:科目番号
	@Id
	@Column(name = "subject_id", length = 2, nullable = false)
	private String subjectId;

	// 論理名称:科目名
	@Column(name = "subject_name", nullable = false)
	private String subjectName;

	// 論理名称:学科番号
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
	private Course course;

	// 論理名称:担当教員
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
	private Staff staff;

	// 空のコンストラクタ
	public Subject() {}

    public Subject(String subjectId, String subjectName, Course course, Staff staff) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.course = course;
        this.staff = staff;
    }

	// ゲッター・セッター
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
}
