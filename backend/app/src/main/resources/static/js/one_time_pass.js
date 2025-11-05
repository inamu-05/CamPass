document.addEventListener("DOMContentLoaded", () => {
  const subjectSelect = document.getElementById("subject");
  const passField = document.getElementById("generated-pass");
  const generateBtn = document.getElementById("generate-btn");
  const saveBtn = document.getElementById("save-btn");
  const backBtn = document.getElementById("back-btn");
  const messageArea = document.getElementById("message-area"); // 🔹 追加: メッセージ表示用

  // 🔹 メッセージ表示用の関数
  function showMessage(text, isError = false) {
    messageArea.textContent = text;
    messageArea.style.color = isError ? "red" : "green";
  }

  // ✅ ランダムな4桁の数字パス生成
  generateBtn.addEventListener("click", () => {
    const subject = subjectSelect.value;
    if (!subject) {
      showMessage("科目を選択してください。", true);
      return;
    }
    const pass = Math.floor(0 + Math.random() * 9000);
    passField.value = pass;
    showMessage("ワンタイムパスが作成されました。", false);
  });

  // ✅ 保存処理（Spring Boot 側に送信）
  saveBtn.addEventListener("click", async () => {
    const subject = subjectSelect.value;
    const pass = passField.value;

    if (!subject) {
      showMessage("科目を選択してください。", true);
      return;
    }
    if (!pass) {
      showMessage("ワンタイムパスを作成してください。", true);
      return;
    }

    const response = await fetch("/save-onetime-pass", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ subject, pass })
    });

    if (response.ok) {
      showMessage("ワンタイムパスを保存しました。", false);
    } else {
      showMessage("保存に失敗しました。", true);
    }
  });

  // ✅ 戻る（メインメニューへ）
  backBtn.addEventListener("click", () => {
    window.location.href = "/templates/base/base.html";
  });
});
