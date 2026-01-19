// In backend/app/src/main/java/com/example/app/service/StudentService.java
package com.example.app.service;

import com.example.app.dto.PromotionCandidateDto;
import com.example.app.entity.ClassGroup;
import com.example.app.entity.Student;
import com.example.app.repository.ClassGroupRepository;
import com.example.app.repository.StudentRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 学生一覧を取得　
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 学生をIDで取得
    public Student getStudentById(String userId) {
        return studentRepository.findById(userId).orElse(null);
    }

    // 学生情報登録
    public Student createStudent(Student student) {
        // パスワードをハッシュ化して保存
        student.setUserPass(passwordEncoder.encode(student.getUserPass()));
        return studentRepository.save(student);
    }
    
    // 学生情報検索
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return studentRepository.findAll();
        } else {
            return studentRepository.findByUserIdContainingOrUserNameContaining(keyword, keyword);
        }
    }
    
    // 学生情報更新
    public Student updateStudent(String userId, Student studentDetails) {
        // 既存の学生情報を取得
        Student existingStudent = getStudentById(userId);
        if (existingStudent == null) {
            return null;
        }

        // 更新情報の取得と設定
        // existingStudent.setUserId(userId);
        existingStudent.setUserName(studentDetails.getUserName());
        existingStudent.setFurigana(studentDetails.getFurigana());
        // existingStudent.setCourseId(studentDetails.getCourseId());
        // existingStudent.setBirth(studentDetails.getBirth());
        // existingStudent.setTel(studentDetails.getTel());
        // existingStudent.setMail(studentDetails.getMail());
        if (studentDetails.getAddress() != null) {
            existingStudent.setAddress(studentDetails.getAddress());            
        }
        // existingStudent.setClassGroup(studentDetails.getClassGroup());
        // existingStudent.setImg(studentDetails.getImg());
        if (studentDetails.getEnrollmentStatus() != null) {
            existingStudent.setEnrollmentStatus(studentDetails.getEnrollmentStatus());            
        }
        // existingStudent.setEntryYear(studentDetails.getEntryYear());
        // existingStudent.setGraduationYear(studentDetails.getGraduationYear());
        if (studentDetails.getIsDisabled() != null) {
            existingStudent.setIsDisabled(studentDetails.getIsDisabled());            
        }

        // // 3. Check if a new password was provided
        // String newPassword = studentDetails.getUserPass();
        // if (newPassword != null && !newPassword.isEmpty()) {
        //     // If yes, hash and set the new password
        //     existingStudent.setUserPass(passwordEncoder.encode(newPassword));
        // }
        // // If no new password was provided, we do nothing (keeping the old one)

        // 学生情報を更新して保存
        return studentRepository.save(existingStudent);
    }

    // // DELETE
    // public void deleteStudent(String userId) {
    //     studentRepository.deleteById(userId);
    // }

    // 進級・卒業処理
    public List<PromotionCandidateDto> getPromotionCandidates(String courseId, String classGroupId) {
        // 在籍区分1(在学)または3(休学)のリストを作成
        List<String> targetStatuses = List.of("1", "3");

        // 対象学生の取得
        List<Student> targtStudents = studentRepository.findByEnrollmentStatusInAndIsDisabledFalse(targetStatuses);

        return targtStudents.stream()
        .filter(s -> {
            // 学科の絞り込み
            boolean courseMatch = (courseId == null || courseId.isEmpty()) || 
                (s.getCourse() != null && s.getCourse().getCourseId().equals(courseId));

            // クラスの絞り込み
            boolean classMatch = (classGroupId == null || classGroupId.isEmpty()) || 
                (s.getClassGroup() != null && s.getClassGroup().getClassGroupId().equals(classGroupId));

            return courseMatch && classMatch;
        })
        .map(s -> {
            String status = s.getEnrollmentStatus();
            String currentClassName = (s.getClassGroup() != null) ? s.getClassGroup().getClassGroup() : ""; // 例: "1-1"
            int grade = 0;
            if (!currentClassName.isEmpty()) {
                grade = Character.getNumericValue(currentClassName.charAt(0)); // 学年を取得（例: "1-1"の'1'を取得）
            }

            String nextClass = "";
            String actionType = "";
            boolean isRecommended = true;

            if ("3".equals(status)) {
                // 休学の場合、次年度も休学扱い
                nextClass = "休学";
                actionType = "STAY";
                isRecommended = false;
            } else if ("1".equals(status)) {
                // 在学の場合、学年に応じて進級または卒業を判定
                if (grade == 1) {   
                    nextClass = currentClassName.replaceFirst("1", "2"); // 例: "2-1"
                    actionType = "PROMOTION";
                } else if (grade == 2) {
                    nextClass = "卒業";
                    actionType = "GRADUATION";
                }
            }
            return new PromotionCandidateDto(
                    s.getUserId(),
                    s.getUserName(),
                    s.getCourse() != null ? s.getCourse().getCourseName() : "",
                    currentClassName,
                    nextClass,
                    status,
                    isRecommended,
                    actionType
            );
        })
        // クラス順・学生番号順に並び替え
        .sorted(Comparator.comparing(PromotionCandidateDto::getCurrentClass).thenComparing(PromotionCandidateDto::getUserId))
        .collect(Collectors.toList());
    }

    @Transactional
    public void executePromotion(List<String> studentIds) {
        // 進級処理
        List<Student> students = studentRepository.findAllById(studentIds);

        for (Student s : students) {
            String status = s.getEnrollmentStatus();
            ClassGroup currentClass = s.getClassGroup();

            if (currentClass == null) continue;

            String currenClassGroup = currentClass.getClassGroup();

            if ("1".equals(status)) {
                if (currenClassGroup.startsWith("1")) {
                    // 1年生から2年生へ進級(例: 1-1 -> 2-1)
                    String newClass = currenClassGroup.replaceFirst("1", "2");
                    ClassGroup nextClassGroup = classGroupRepository.findByClassGroup(newClass).orElseThrow(() -> new RuntimeException("次年度のクラスが見つかりません: " + newClass));
                    s.setClassGroup(nextClassGroup);
                } else if (currenClassGroup.startsWith("2")) {
                    // 2年生から卒業へ
                    s.setEnrollmentStatus("2"); // 卒業に設定
                    s.setIsDisabled(true); // 学生証を無効化
                    s.setGraduationYear(String.valueOf(LocalDate.now().getYear()));
                }
            }
        }
    }
}