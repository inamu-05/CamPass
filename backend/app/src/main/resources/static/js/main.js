document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".dropbtn").forEach(button => {
    button.addEventListener("click", () => {
      const content = button.nextElementSibling;

      // 表示・非表示切り替え
      if (content.style.display === "block") {
        content.style.display = "none";
        button.textContent = button.textContent.replace('▲','▼');
      } else {
        content.style.display = "block";
        button.textContent = button.textContent.replace('▼','▲');
      }
    });
  });
});
