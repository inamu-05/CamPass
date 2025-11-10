document.addEventListener("DOMContentLoaded", () => {
  const cancelBtn = document.getElementById("cancel-btn");
  const form = document.getElementById("update-form");

  // ✅ localStorage から選択された学生データを取得
  const stored = localStorage.getItem("selectedStudent");
  if (stored) {
    const student = JSON.parse(stored);
    document.getElementById("student-id").value = student.id;
    document.getElementById("student-name").value = student.name;
    document.getElementById("student-class").value = student.class_name;
  }

    // 戻るボタン
  cancelBtn.addEventListener("click", () => {
    window.location.href = "/student/search"; // ✅ Springのルーティングに合わせる
  });

  // 更新ボタン（フォーム送信）
  form.addEventListener("submit", (e) => {
    e.preventDefault();

    // ✅ 完了後に localStorage を削除
    localStorage.removeItem("selectedStudent");

    // ✅ 完了画面へ遷移（あとでcontrollerを追加する予定）
    window.location.href = "/student/update/comp"; 
  });
});
