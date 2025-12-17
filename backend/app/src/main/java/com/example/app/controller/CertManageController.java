package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app.entity.CertManage;
import com.example.app.repository.CertMRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class CertManageController {

    @Autowired
    private CertMRepository certMRepository;

    // -------------------------
    // 証明書一覧表示
    // -------------------------
    @GetMapping("/cert/list")
    public String showManage(Model model) {

        List<CertManage> unprintedList = certMRepository.findByIsPrintedFalseOrderByRequestedOnDesc();
        List<CertManage> printedList = certMRepository.findByIsPrintedTrueOrderByRequestedOnDesc();
        int pendingCount = unprintedList.size();

        model.addAttribute("unprintedList", unprintedList);
        model.addAttribute("printedList", printedList);
        model.addAttribute("pendingCount", pendingCount);

        return "main/certificate_list";
    }

    // -------------------------
    // 発行ボタン押下処理
    // -------------------------
    @PostMapping("/cert/issue/{id}")
    public String issueCertificate(@PathVariable("id") Integer applicationId) {

        // DBから該当データ取得
        CertManage cert = certMRepository.findById(applicationId).orElse(null);
        if (cert != null && !cert.getIsPrinted()) {
            cert.setIsPrinted(true); // false → true に更新
            certMRepository.save(cert); // DB保存
        }

        // 再度一覧ページへリダイレクト
        return "redirect:/cert/list";
    }
}
