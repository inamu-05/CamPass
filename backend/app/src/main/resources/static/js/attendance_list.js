document.addEventListener("DOMContentLoaded", () => {
    const dateInput = document.getElementById("date");

    dateInput.addEventListener("input", (e) => {
        let v = e.target.value;

        // 入力形式が yyyy-MM-dd の途中の場合を考慮
        // 数字のみ抽出
        const digits = v.replace(/\D/g, "");

        if (digits.length > 4) {
            // 5桁以上入力されたら 4桁 + '-' + 残り に自動整形
            const year = digits.substring(0, 4);       // 最初の4桁
            const rest = digits.substring(4);          // 5桁目以降

            // 月の先頭入力を自動で入れてあげる
            if (rest.length === 1) {
                e.target.value = `${year}-${rest}`;
            } else if (rest.length >= 2) {
                e.target.value = `${year}-${rest.substring(0, 2)}`;
            }
        }
    });
});
