package com.example.app.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
@Table(name = "attendance")
public class Attendance {

	// 複合キー（科目番号+学生番号）
	@EmbeddedId
	private AttendanceId id;

	// 論理名称:科目番号
	@MapsId("subjectId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id", nullable = false)
	private Subject subject;

	// 論理名称:学生番号
	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Student student;

	// 論理名称:出席日時
	@Column(name = "attendance_on")
	private LocalDateTime attendanceOn;

	// 出欠（FALSE:欠席、TRUE:出席）
	@Column(name = "is_attended", nullable = false)
	private Boolean isAttended=false;

	// 備考（1:遅刻、2:早退）
	@Column(name = "remark", length = 1)
	private String remark;

	// 空のコンストラクタ
	public Attendance() {}

	// ゲッター・セッター
	public AttendanceId getId() {
		return id;
	}
}

