document.addEventListener("DOMContentLoaded", () => {
  const okBtn = document.getElementById("ok-btn");

   // ✅ 共通ヘッダー読み込み
    fetch("/templates/base/header.html")
      .then(res => res.text())
      .then(html => {
        document.getElementById("header-container").innerHTML = html;
      })
      .catch(err => console.error("ヘッダー読み込み失敗:", err));
    
  okBtn.addEventListener("click", () => {
    // ✅ 学生情報検索画面へ遷移
    window.location.href = "student_search.html";
  });
});
