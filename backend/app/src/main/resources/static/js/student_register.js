document.addEventListener("DOMContentLoaded", () => {
  const input = document.querySelector("input[name='file']");
  const preview = document.getElementById("previewImage");

  if (input && preview) {
    input.addEventListener("change", () => {
      const file = input.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = e => {
          preview.src = e.target.result;
          preview.style.display = "block";
        };
        reader.readAsDataURL(file);
      } else {
        preview.style.display = "none";
        preview.src = "";
      }
    });
  }
});
