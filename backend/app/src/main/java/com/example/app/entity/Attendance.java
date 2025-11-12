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
	@Column(name = "attended_on")
	private LocalDateTime attendedOn;

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
    public void setId(AttendanceId id) {
		this.id = id;
	}
    // public String getSubjectId() {
    //     return subject.getSubjectId();
    // }
    public void setSubjectId(String subjectId) {
        subject.setSubjectId(subjectId);
    }
    // public String getUserId() {
    //     return student.getUserId();
    // }
    public void setUserId(String userId) {
        student.setUserId(userId);
    }
    // public LocalDateTime getSessionDatetime() {
    //     return id.getSessionDatetime();
    // }
    // public void setSessionDatetime(LocalDateTime sessionDatetime) {
    //     id.setSessionDatetime(sessionDatetime);
    // }
    public LocalDateTime getAttendedOn() {
        return attendedOn;
    }
    public void setAttendedOn(LocalDateTime attendedOn) {
        this.attendedOn = attendedOn;
    }
    public Boolean getIsAttended() {
        return isAttended;
    }
    public void setIsAttended(Boolean isAttended) {
        this.isAttended = isAttended;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Subject getSubject() {
        return subject;
    }
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    public String getSubjectId() {
        return subject != null ? subject.getSubjectId() : null;
    }
    public String getUserId() {
        return student != null ? student.getUserId() : null;
    }
    public LocalDateTime getSessionDatetime() {
        return id != null ? id.getSessionDatetime() : null;
    }
    public void setSessionDatetime(LocalDateTime sessionDatetime) {
        if (this.id == null) {
            this.id = new AttendanceId();
        }
        this.id.setSessionDatetime(sessionDatetime);
    }
}

