package com.example.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.entity.CertManage;
import com.example.app.repository.CertMRepository;


@Service
public class CertMService {

    @Autowired
    private CertMRepository certMRepository;

    // 未発行証明書一覧を取得
    public List<CertManage> getUnprintedCertificates() {
        List<CertManage> list = certMRepository.findByIsPrintedFalseOrderByRequestedOnDesc();

        // System.out.print で確認
        System.out.println("Service: 未発行証明書リスト件数 = " + list.size());
        if (!list.isEmpty()) {
            System.out.println("Service: 1件目の申請ID = " + list.get(0).getApplicationId());
            System.out.println("Service: 1件目の学生ID = " + list.get(0).getStudent().getUserId());
        }

        return list;
    }

    // 発行済み証明書一覧を取得
    public List<CertManage> getPrintedCertificates() {
        List<CertManage> list = certMRepository.findByIsPrintedTrueOrderByRequestedOnDesc();

        // System.out.print で確認
        System.out.println("Service: 発行済み証明書リスト件数 = " + list.size());
        if (!list.isEmpty()) {
            System.out.println("Service: 1件目の申請ID = " + list.get(0).getApplicationId());
            System.out.println("Service: 1件目の学生ID = " + list.get(0).getStudent().getUserId());
        }

        return list;
    }

    // 未発行件数を取得
    public int getPendingCount() {
        int count = getUnprintedCertificates().size();
        System.out.println("Service: 未発行件数 = " + count);
        return count;
    }

    @Transactional
    public boolean markAsPrinted(Integer certId) {
        Optional<CertManage> certOpt = certMRepository.findById(certId);
        if (certOpt.isPresent()) {
            CertManage cert = certOpt.get();
            cert.setIsPrinted(true);
            certMRepository.save(cert); // 更新
            System.out.println("Service: 証明書ID " + certId + " を発行済みに更新しました");
            return true;
        } else {
            System.out.println("Service: 証明書ID " + certId + " は存在しません");
            return false;
        }
    }
}
