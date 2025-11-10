document.addEventListener("DOMContentLoaded", () => {
  const input = document.getElementById("photo");
  const preview = document.getElementById("photo-preview");

  if (input) {
    input.addEventListener("change", () => {
      const file = input.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = e => {
          preview.innerHTML = `<img src="${e.target.result}" alt="写真" class="preview-img">`;
        };
        reader.readAsDataURL(file);
      }
    });
  }
});
