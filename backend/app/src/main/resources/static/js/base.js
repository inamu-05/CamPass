document.addEventListener("DOMContentLoaded", () => {
  // HTMLはThymeleafでサーバー側挿入済みなので、直接イベントをセットすればOK
  setupMainEvents();
  setupDropdowns();
});

// ===== メインイベント設定 =====
function setupMainEvents() {
  const registerBtn = document.getElementById("btn-student-register");
  if (registerBtn) {
    registerBtn.addEventListener("click", () => {
      window.location.href = "main/student/register"; // ✅ コントローラで定義したURL
    });
  }
  
  // 学生情報更新ボタン
  const updateBtn = document.getElementById("btn-student-update");
  if (updateBtn) {
    updateBtn.addEventListener("click", () => {
      window.location.href = "/student/search";
    });
  }

  // ワンタイムパス作成
  const passBtn = document.getElementById("btn-pass-create");
  if (passBtn) {
    passBtn.addEventListener("click", () => {
      window.location.href = "/onetimepass";
    });
  }

  // 未実装のボタンたち
  const attendBtn = document.getElementById("btn-attendance-check");
  const certBtn = document.getElementById("btn-certificate-approve");

  [attendBtn, certBtn].forEach(btn => {
    if (btn) {
      btn.addEventListener("click", () => {
        alert("この機能は現在準備中です。");
      });
    }
  });
}

// ===== ドロップダウン制御 =====
function setupDropdowns() {
  const dropdownButtons = document.querySelectorAll('.dropbtn');

  dropdownButtons.forEach(button => {
    button.addEventListener('click', function () {
      const dropdown = this.parentElement;
      const isOpen = dropdown.classList.contains('open');
      document.querySelectorAll('.dropdown').forEach(d => d.classList.remove('open'));
      if (!isOpen) dropdown.classList.add('open');
    });
  });

  document.addEventListener('click', function (event) {
    if (!event.target.closest('.dropdown')) {
      document.querySelectorAll('.dropdown').forEach(d => d.classList.remove('open'));
    }
  });
}
