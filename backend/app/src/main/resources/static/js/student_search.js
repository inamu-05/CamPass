document.addEventListener("DOMContentLoaded", () => {
  const searchBtn = document.getElementById("search-btn");
  const searchInput = document.getElementById("search-input");
  const tableBody = document.querySelector("#student-table tbody");

  // ✅ 共通ヘッダー読み込み
    fetch("/templates/base/header.html")
      .then(res => res.text())
      .then(html => {
        document.getElementById("header-container").innerHTML = html;
      })
      .catch(err => console.error("ヘッダー読み込み失敗:", err));
    
  searchBtn.addEventListener("click", () => {
    const query = searchInput.value.trim();

    // 仮データ
    const students = [
      { id: "S12345", name: "山田 太郎", class_name: "情報システム科A" },
      { id: "S67890", name: "佐藤 花子", class_name: "情報システム科B" }
    ];

    // 結果をクリア
    tableBody.innerHTML = "";

    // テーブルに追加
    students.forEach(student => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${student.id}</td>
        <td>${student.name}</td>
        <td>${student.class_name}</td>
        <td><button class="update-btn" data-id="${student.id}">更新</button></td>
      `;
      tableBody.appendChild(row);
    });

    // 更新ボタンイベント
    document.querySelectorAll(".update-btn").forEach(button => {
      button.addEventListener("click", (e) => {
        const studentId = e.target.dataset.id;
        const student = students.find(s => s.id === studentId);

        // ✅ 選択した学生データを保存
        localStorage.setItem("selectedStudent", JSON.stringify(student));

        // ✅ 更新画面へ遷移
        window.location.href = "student_update.html";
      });
    });
  });
});
