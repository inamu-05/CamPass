package com.example.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dto.CertificateHistoryDto;
import com.example.app.dto.CertManageRequest;
import com.example.app.entity.CertManage;
import com.example.app.entity.Certificate;
import com.example.app.entity.Student;
import com.example.app.repository.CertManageRepository;
import com.example.app.repository.CertificateRepository;
import com.example.app.repository.StudentRepository;
import java.util.Comparator;


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

    public List<CertificateHistoryDto> getHistoryByStudentId(String studentId) {

        List<CertManage> list = 
                certManageRepository.findHistoryWithCertificate(studentId);

        return list.stream().map(cm -> {
            CertificateHistoryDto dto = new CertificateHistoryDto();
            dto.setPurchaseDate(cm.getRequestedOn().toString());
            dto.setName(cm.getCertificate().getCertificateName());
            dto.setPrice(cm.getCertificate().getPrice());
            dto.setQuantity(cm.getQuantity());
            dto.setPayment(convertPayment(cm.getPayment()));
            dto.setReceive(convertReceive(cm.getReceive()));
            dto.setStatus(convertSituationByPrinted(cm.getIsPrinted()));
            dto.setSituationOrder(situationOrderByPrinted(cm.getIsPrinted()));
            return dto;
        })
        //並べ替え
        .sorted(
            Comparator.comparingInt(
                CertificateHistoryDto::getSituationOrder
            ).thenComparing(
                CertificateHistoryDto::getPurchaseDate,
            Comparator.reverseOrder()
            )
        )
        .toList();
    }

    private String convertPayment(String payment) {
        return switch (payment) {
            case "1" -> "学校支払";
            case "2" -> "コンビニ支払";
            case "3" -> "PayPay";
            default -> "不明";
        };
    }

    private String convertReceive(String receive) {
        return switch (receive) {
            case "1" -> "窓口受取";
            case "2" -> "郵送";
            case "3" -> "データ配布";
            default -> "不明";
        };
    }

    private String convertSituationByPrinted(Boolean isPrinted) {
        if (isPrinted == null || !isPrinted) {
            return "支払済";
        }
        return "受取済";
    }   //発行フラグから状態を判断することにします

    // private String convertSituation(String situation) {
    //     return switch (situation) {
    //         case "0" -> "申請中";
    //         case "1" -> "支払済";
    //         case "2" -> "受取済";
    //         default -> "不明";
    //     };
    // }

    private int situationOrderByPrinted(Boolean isPrinted) {
        if (isPrinted == null || !isPrinted) {
            return 1; // 支払済（上）
        }
        return 2;     // 受取済（下）
    }

    // private int situationOrder(String situation) { // 並べ替え用
    //     return switch (situation) {
    //         case "0" -> 1; // 申請中
    //         case "1" -> 2; // 支払済
    //         case "2" -> 3; // 受取済
    //         default -> 4;  // 不明
    //     };
    // }

}
