package com.example.app.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "cert_manage")
public class CertManage {

	// 複合キー（証明書番号+学生番号）
	@EmbeddedId
	private CertManageId id;

	// 論理名称:証明書番号
	@MapsId("certificateId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "certificate_id",nullable = false)
	private Certificate certificate;

	// 論理名称:学生番号
	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Student student;

	// 論理名称:部数
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	// 論理名称:受取方法
	@Column(name = "receive", length = 1, nullable = false)
	private String receive;

	// 論理名称:支払方法
	@Column(name = "payment", length = 1, nullable = false)
	private String payment;

	// 論理名称:申請日
	@Column(name = "requested_on", nullable = false)
	private LocalDate requestedOn;

	// 論理名称:発行フラグ
	@Column(name = "is_printed", nullable = false)
	private Boolean isPrinted=false;

	// 論理名称:状況
	@Column(name = "situation", length = 1, nullable = false)
	private String situation;

	// 空のコンストラクタ
	public CertManage() {}

	// ゲッター・セッター
	public CertManageId getId() {
		return id;
	}
	public void setId(CertManageId id) {
		this.id = id;
	}
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getReceive() {
		return receive;
	}
	public void setReceive(String receive) {
		this.receive = receive;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public LocalDate getRequestedOn() {
		return requestedOn;
	}
	public void setRequestedOn(LocalDate requestedOn) {
		this.requestedOn = requestedOn;
	}
	public Boolean getIsPrinted() {
		return isPrinted;
	}
	public void setIsPrinted(Boolean isPrinted) {
		this.isPrinted = isPrinted;
	}
	public String getSituation() {
		return situation;
	}
	public void setSituation(String situation) {
		this.situation = situation;
	}
}
