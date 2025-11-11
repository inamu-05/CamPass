document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector(".search-form");
  const subjectInput = document.getElementById("subject");
  const dateInput = document.getElementById("date");
  const tableBody = document.querySelector(".attendance-table tbody");

  // === 仮データ ===
  const mockData = [
    { id: 1, date: "2025-11-01", subjectName: "情報処理", attendanceCount: 25 },
    { id: 2, date: "2025-11-02", subjectName: "データベース", attendanceCount: 28 },
    { id: 3, date: "2025-11-03", subjectName: "情報処理", attendanceCount: 27 },
    { id: 4, date: "2025-11-04", subjectName: "ネットワーク基礎", attendanceCount: 23 },
  ];

  // === 検索ボタンが押されたときの処理 ===
  form.addEventListener("submit", (e) => {
    e.preventDefault(); // ページ遷移を防止

    const subject = subjectInput.value;
    const date = dateInput.value.trim();

    // === 条件に合うデータを抽出 ===
    const filtered = mockData.filter(item => {
      const matchSubject = subject === "" || item.subjectName.includes(subject);
      const matchDate = date === "" || item.date === date;
      return matchSubject && matchDate;
    });

    // === テーブル描画 ===
    renderTable(filtered);
  });

  // === テーブル表示関数 ===
  function renderTable(data) {
    tableBody.innerHTML = ""; // 一旦リセット

    if (data.length === 0) {
      tableBody.innerHTML = "<tr><td colspan='4'>該当するデータがありません。</td></tr>";
      return;
    }

    data.forEach(record => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${record.date}</td>
        <td>${record.subjectName}</td>
        <td>${record.attendanceCount}</td>
        <td><button class="detail-btn" data-id="${record.id}">詳細</button></td>
      `;
      tableBody.appendChild(row);
    });

    // === 詳細ボタン処理 ===
    document.querySelectorAll(".detail-btn").forEach(button => {
      button.addEventListener("click", (e) => {
        const recordId = e.target.dataset.id;
        const record = mockData.find(r => r.id == recordId);

        if (!record) return;

        // ✅ データを localStorage に保存（詳細画面で使用可能にする）
        localStorage.setItem("selectedRecord", JSON.stringify(record));

        // ✅ 詳細画面に遷移
        window.location.href = "/attendance/detail";
      });
    });
  }

  // 初期表示：全件表示
  renderTable(mockData);
});
