package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PromotionCandidateDto {
    private String userId;        // 学籍番号
    private String userName;      // 氏名
    private String courseName;    // 所属学科
    private String currentClass;  // 現在のクラス (例: 1-1)
    private String nextClass;     // 次年度の予測 (例: 2-1 または 卒業)
    private String status;        // 在籍区分 (1:在学, 2:卒業, 3:休学, 4:退学)
    private boolean isRecommended;// デフォルトでチェックを入れるか
    private String actionType;    // 処理種別 (PROMOTION, GRADUATION, INVALIDATE)
}