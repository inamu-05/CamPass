function confirmPromotion() {
    const selectedChecks = document.querySelectorAll('input[name="studentIds"]:checked');
    
    // 未選択の場合はダイアログを出さずにサーバーへ送信（サーバー側のバリデーションへ）
    if (selectedChecks.length === 0) {
        return true; 
    }

    let counts = { "進級": 0, "卒業": 0 };

    selectedChecks.forEach(check => {
        const row = check.closest('tr');
        const typeCellText = row.querySelector('td:last-child').textContent.trim();
        
        if (typeCellText.includes("進級")) {
            counts["進級"]++;
        } else if (typeCellText.includes("卒業")) {
            counts["卒業"]++;
        }
    });

    const message = `選択した学生の更新を実行しますか？\n\n` +
                    `【内訳】\n` +
                    `進級：${counts["進級"]}名\n` +
                    `卒業：${counts["卒業"]}名\n` +
                    `合計：${selectedChecks.length}名`;

    return confirm(message);
}