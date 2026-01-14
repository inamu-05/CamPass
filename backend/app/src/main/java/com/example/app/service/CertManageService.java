package com.example.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dto.CertManageRequest;
import com.example.app.entity.CertManage;
import com.example.app.entity.Certificate;
import com.example.app.entity.Student;
import com.example.app.repository.CertManageRepository;
import com.example.app.repository.CertificateRepository;
import com.example.app.repository.StudentRepository;


@Service
public class CertManageService {

    @Autowired
    private CertManageRepository certManageRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CertificateRepository certificateRepository;

    // ▼ 新規申請登録
    public CertManage save(CertManage certManage) {
        return certManageRepository.save(certManage);
    }

    // ▼ 学生IDごとの申請履歴一覧
    public List<CertManage> findByStudentId(String studentId) {
        return certManageRepository.findByStudent_UserId(studentId);
    }

    // ▼ application_id で1件取得（必要なら）
    public Optional<CertManage> findById(Integer applicationId) {
        return certManageRepository.findById(applicationId);
    }

    // ▼ 全件取得（必要なら）
    public List<CertManage> findAll() {
        return certManageRepository.findAll();
    }

    // ▼ 新規申請登録（DTO版）
    public CertManage createApplication(String studentId, CertManageRequest request) {

        // ------------------------
        // ① Student（外部キー）を取得
        // ------------------------
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生が見つかりません"));

        // ------------------------
        // ② Certificate（外部キー）を取得
        // ------------------------
        Certificate certificate = certificateRepository
                .findById(request.getCertificateId())
                .orElseThrow(() -> new RuntimeException("証明書が見つかりません"));

        // ------------------------
        // ③ 新規申請データを作成
        // ------------------------
        CertManage certManage = new CertManage();

        certManage.setStudent(student);           // ← ★IDではなくエンティティをセット！
        certManage.setCertificate(certificate);   // ← ★こっちも同様！

        certManage.setQuantity(request.getQuantity());
        certManage.setReceive(request.getReceive());
        certManage.setPayment(request.getPayment());
        certManage.setRequestedOn(LocalDate.now());
        certManage.setIsPrinted(false);
        certManage.setSituation("0"); // 未発行

        // ------------------------
        // ④ データベースへ保存
        // ------------------------
        return certManageRepository.save(certManage);
    }

}
