document.addEventListener("DOMContentLoaded", () => {
  setupMainEvents();
  setupDropdowns();
});

// ===== メインイベント設定 =====
function setupMainEvents() {
  const registerBtn = document.getElementById("btn-student-register");
  if (registerBtn) {
    registerBtn.addEventListener("click", () => {
      window.location.href = "/student/register";
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

  const attendBtn = document.getElementById("btn-attendance-check");
  if (attendBtn) {
    attendBtn.addEventListener("click", () => {
      window.location.href = "/student/attendance";
    });
  }

  const certBtn = document.getElementById("btn-certificate-approve");
  if (certBtn) {
    certBtn.addEventListener("click", () => {
      window.location.href = "/cert/list";
    });
  }
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
