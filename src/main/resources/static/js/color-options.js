document.addEventListener("DOMContentLoaded", () => {
    const colorButtons = document.querySelectorAll(".color-btn");
    const sizeOptions = document.getElementById("size-options");
    const productId = "${product.id}"; // Lấy id từ server truyền vào

    colorButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const color = btn.dataset.color;

            fetch(`/api/products/${productId}/sizes?color=${color}`)
                .then(res => res.json())
                .then(sizes => {
                    sizeOptions.innerHTML = "";
                    sizes.forEach(size => {
                        const btnSize = document.createElement("button");
                        btnSize.type = "button";
                        btnSize.className = "size-btn";
                        btnSize.dataset.size = size;
                        btnSize.innerText = "VN " + size;
                        sizeOptions.appendChild(btnSize);
                    });
                })
                .catch(err => console.error(err));
        });
    });
});
