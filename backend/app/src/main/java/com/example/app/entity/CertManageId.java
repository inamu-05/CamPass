package com.example.app.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class CertManageId implements Serializable{

    // 証明書番号
    @Column(name = "certificate_id", length = 2)
    private String certificateId;

    // 学生番号
    @Column(name = "user_id")
    private String userId;

    // 空のコンストラクタ
    public CertManageId() {}

    // ゲッター・セッター
    public String getCertificateId() {
        return certificateId;
    }
    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CertManageId)) return false;
        CertManageId that = (CertManageId) o;
        return Objects.equals(certificateId, that.certificateId) &&
               Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateId, userId);
    }
}
