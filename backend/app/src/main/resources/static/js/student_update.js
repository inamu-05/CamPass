document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("update-form");
  const statusSelect = document.getElementById("student-class");
  const disableCheckbox = document.querySelector('input[name="isDisabled"]');

  // ✅ 退学(4)を選択した際に「学生証無効化」にチェックを入れる
  if (statusSelect && disableCheckbox) {
    statusSelect.addEventListener("change", () => {
      if (statusSelect.value === "4") {
          disableCheckbox.checked = true;
      }
    });
  }

  // ✅ フォーム送信時の処理
  if (form) {
    form.addEventListener("submit", (e) => {
      // localStorageのクリーンアップが必要な場合のみ実行
      localStorage.removeItem("selectedStudent");
      console.log("フォームを送信します。");
    });
  }
});