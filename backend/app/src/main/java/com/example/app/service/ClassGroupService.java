package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.app.entity.ClassGroup;
import com.example.app.repository.ClassGroupRepository;

@Service
public class ClassGroupService {

    @Autowired
    private ClassGroupRepository classGroupRepository;

    // クラス取得メソッド　
    public List<ClassGroup> getAllClassGroups() {
        return classGroupRepository.findAll();
    }
}
