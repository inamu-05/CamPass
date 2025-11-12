document.addEventListener("DOMContentLoaded", () => {
  const subjectSelect = document.getElementById("subject");
  const passField = document.getElementById("generated-pass");
  const generateBtn = document.getElementById("generate-btn");
  const saveBtn = document.getElementById("save-btn");
  const backBtn = document.getElementById("back-btn");
  const messageArea = document.getElementById("message-area"); // 🔹 追加: メッセージ表示用


  function formatToFourDigits(num) {
    return String(num).padStart(4, '0');
  }

// console.log(formatToFourDigits(7));

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
    const pass = formatToFourDigits(Math.floor(Math.random() * 10000));
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

    // Get the CSRF token (Important for Spring Security POST)
    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const response = await fetch("/save-onetime-pass", {
      method: "POST",
      headers: { 
            "Content-Type": "application/json",
            [header]: token // Add CSRF token to header
        },
      body: JSON.stringify({ subjectId: subject, pass: pass }) // Send correct JSON
    })
    
    if (response.ok) {
        console.log('OTP saved successfully via browser!');
        showMessage("ワンタイムパスを保存しました。", false);
    } else {
        console.error('Failed to save OTP:', response.statusText);
        showMessage("保存に失敗しました。", true);
    }
  });

  // ✅ 戻る（メインメニューへ）
  backBtn.addEventListener("click", () => {
    window.location.href = "/main";
  });
});
