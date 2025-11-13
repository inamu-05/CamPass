document.addEventListener("DOMContentLoaded", () => {
  // === 仮データ ===
  const pendingList = [
    { date: "2025/11/10", id: "202501", name: "田中 太郎", doc: "在学証明書", copies: 1, receive: "窓口受取" },
    { date: "2025/11/09", id: "202502", name: "佐藤 花子", doc: "成績証明書", copies: 2, receive: "郵送" },
  ];

  const approvedList = [
    { date: "2025/11/05", id: "202498", name: "山田 一郎", doc: "卒業証明書", copies: 1, receive: "窓口受取" },
  ];

  const pendingTable = document.querySelector("#pendingTable tbody");
  const approvedTable = document.querySelector("#approvedTable tbody");
  const pendingCount = document.getElementById("pendingCount");

  // === 未発行件数の更新 ===
  const updatePendingCount = () => {
    pendingCount.textContent = `未発行件数：${pendingList.length}件`;
  };

  // === 未発行リスト描画 ===
  const renderPending = () => {
    pendingTable.innerHTML = "";
    pendingList.forEach((item, index) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.date}</td>
        <td>${item.id}</td>
        <td>${item.name}</td>
        <td>${item.doc}</td>
        <td>${item.copies}</td>
        <td>${item.receive}</td>
        <td><button class="approve-btn" data-index="${index}">発行</button></td>
      `;
      pendingTable.appendChild(tr);
    });
    updatePendingCount();
  };

  // === 発行済リスト描画 ===
  const renderApproved = () => {
    approvedTable.innerHTML = "";
    approvedList.forEach((item) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.date}</td>
        <td>${item.id}</td>
        <td>${item.name}</td>
        <td>${item.doc}</td>
        <td>${item.copies}</td>
        <td>${item.receive}</td>
        <td class="status-approved">発行済</td>
      `;
      approvedTable.appendChild(tr);
    });
  };

  // 初期表示
  renderPending();
  renderApproved();

  // === 発行ボタンのクリック処理 ===
  document.addEventListener("click", (e) => {
    if (e.target.classList.contains("approve-btn")) {
      const index = e.target.dataset.index;
      const approvedItem = pendingList.splice(index, 1)[0];
      approvedList.push(approvedItem);

      renderPending();
      renderApproved();
    }
  });
});
