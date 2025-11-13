package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class Class {
    
    @Id
    @Column(name = "class_group_id", length=2, nullable = false)
    private String classGroupId;

    @Column(name = "class_group", length=3, nullable = false)
    private String classGroup;

    // 空のコンストラクタ
    public Class() {}

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

