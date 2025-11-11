document.addEventListener("DOMContentLoaded", () => {
  const record = JSON.parse(localStorage.getItem("selectedRecord"));
  const classInfo = document.getElementById("class-info");
  const tableBody = document.getElementById("studentTableBody");

  if (!record) {
    classInfo.innerHTML = "<p>データが見つかりません。</p>";
    return;
  }

  // === 授業情報を表示 ===
  classInfo.innerHTML = `
    <p><strong>日付：</strong> ${record.date}</p>
    <p><strong>授業名：</strong> ${record.subjectName}</p>
    <p><strong>出席人数：</strong> ${record.attendanceCount}</p>
  `;

  // === 仮学生データ ===
  // ※実際はバックエンド連携で取得
  const studentData = [
    { studentId: "S001", name: "田中 太郎", status: "出席", remark: "" },
    { studentId: "S002", name: "佐藤 花子", status: "遅刻", remark: "10分遅刻" },
    { studentId: "S003", name: "鈴木 一郎", status: "早退", remark: "途中退室" },
    { studentId: "S004", name: "高橋 真美", status: "出席", remark: "" },
  ];

  // === テーブル描画 ===
  renderStudentTable(studentData);

  function renderStudentTable(data) {
    tableBody.innerHTML = "";
    if (data.length === 0) {
      tableBody.innerHTML = "<tr><td colspan='4'>学生データがありません。</td></tr>";
      return;
    }

    data.forEach(student => {
      const tr = document.createElement("tr");

      const remarkText = student.remark ? student.remark : "なし";

      tr.innerHTML = `
        <td>${student.studentId}</td>
        <td>${student.name}</td>
        <td>${student.status}</td>
        <td>${remarkText}</td>
      `;

      tableBody.appendChild(tr);
    });
  }
});
