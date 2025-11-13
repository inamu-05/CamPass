package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "class_group")
public class ClassGroup {
    
    @Id
    @Column(name = "class_group_id", length=2, nullable = false)
    private String classGroupId;

    @Column(name = "class_group", length=3, nullable = false)
    private String classGroup;

    // 空のコンストラクタ
    public ClassGroup() {}
    
    // 全フィールドコンストラクタ
    public ClassGroup(String classGroupId, String classGroup) {
        this.classGroupId = classGroupId;
        this.classGroup = classGroup;
    }

    // ゲッター・セッター
    public String getClassGroupId() {
        return classGroupId;
    }
    public void setClassGroupId(String classGroupId) {
        this.classGroupId = classGroupId;
    }
    public String getClassGroup() {
        return classGroup;
    }
    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
    }
}

