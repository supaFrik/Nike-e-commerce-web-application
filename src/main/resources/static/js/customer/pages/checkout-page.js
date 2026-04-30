(function () {
  const ctx = (window.APP_CTX || "").replace(/\/$/, "");
  const shippingRadios = document.querySelectorAll('input[name="shippingMethod"]');
  const paymentRadios = document.querySelectorAll('input[name="pay-method"]');
  const subtotalValue = parseFloat(document.getElementById("subtotalValue")?.value || "0") || 0;
  const discountValue = parseFloat(document.getElementById("discountValue")?.value || "0") || 0;
  const shippingCostDisplay = document.getElementById("shippingCostDisplay");
  const totalDisplay = document.getElementById("totalDisplay");
  const primarySubmit = document.getElementById("primary-submit");
  const terms = document.getElementById("terms");
  const addressSelect = document.getElementById("addressId");
  const recipientNameInput = document.getElementById("recipientName");
  const phoneInput = document.getElementById("phone");
  const line1Input = document.getElementById("line1");
  const line2Input = document.getElementById("line2");
  const cityInput = document.getElementById("city");
  const provinceInput = document.getElementById("province");
  const postalCodeInput = document.getElementById("postalCode");
  const countryInput = document.getElementById("country");
  const noteInput = document.getElementById("note");
  const errorBox = document.getElementById("checkoutError");
  const cardFields = document.getElementById("card-fields");
  const vnpayFields = document.getElementById("vnpay-fields");
  const cardBrands = document.getElementById("card-brands");
  const bankCodeInput = document.getElementById("bank-code");
  const paymentLanguageInput = document.getElementById("payment-language");
  const modal = document.getElementById("vnpay-modal");
  const vnpayConfirm = document.getElementById("vnpay-confirm");
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;
  const formatter = new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" });

  function currentShippingRadio() {
    return document.querySelector('input[name="shippingMethod"]:checked');
  }

  function updateShippingSummary() {
    const selected = currentShippingRadio();
    const shippingCost = parseFloat(selected?.dataset.cost || "0") || 0;
    const total = subtotalValue + shippingCost - discountValue;

    document.querySelectorAll(".delivery-option").forEach((option) => option.classList.remove("selected"));
    selected?.closest(".delivery-option")?.classList.add("selected");

    if (shippingCostDisplay) {
      shippingCostDisplay.textContent = formatter.format(shippingCost);
    }
    if (totalDisplay) {
      totalDisplay.textContent = formatter.format(total);
    }
  }

  function currentPaymentMethod() {
    return document.querySelector('input[name="pay-method"]:checked')?.value || "cod";
  }

  function updateSelectedAddressSummary() {
    const summary = document.getElementById("selectedAddressSummary");
    const selectedOption = addressSelect?.selectedOptions?.[0];
    if (!summary) {
      return;
    }

    if (selectedOption && selectedOption.value) {
      recipientNameInput.value = selectedOption.dataset.recipient || "";
      phoneInput.value = selectedOption.dataset.phone || "";
      line1Input.value = selectedOption.dataset.line1 || "";
      line2Input.value = selectedOption.dataset.line2 || "";
      cityInput.value = selectedOption.dataset.city || "";
      provinceInput.value = selectedOption.dataset.province || "";
      postalCodeInput.value = selectedOption.dataset.postal || "";
      countryInput.value = selectedOption.dataset.country || "";
    }

    const parts = [
      line1Input.value,
      line2Input.value,
      cityInput.value,
      provinceInput.value,
      postalCodeInput.value,
      countryInput.value
    ].filter(Boolean);

    summary.innerHTML = ""
      + `<strong>${recipientNameInput.value || "Địa chỉ giao hàng"}</strong>`
      + `<p>${parts.join(", ") || "Thông tin sẽ được lấy từ form bạn đang nhập."}</p>`
      + `<p>${phoneInput.value || ""}</p>`;
  }

  function switchPaymentMode() {
    const mode = currentPaymentMethod();
    cardFields?.classList.add("hidden");
    vnpayFields?.classList.toggle("hidden", mode !== "vnpay");
    cardBrands?.classList.add("hidden");
    if (primarySubmit) {
      primarySubmit.textContent = mode === "vnpay" ? "Thanh toán bằng VNPay QR" : "Hoàn tất thanh toán";
    }
  }

  function showError(message) {
    if (!errorBox) {
      return;
    }
    errorBox.textContent = message;
    errorBox.style.display = "block";
  }

  function clearError() {
    if (!errorBox) {
      return;
    }
    errorBox.textContent = "";
    errorBox.style.display = "none";
  }

  function showModal() {
    modal?.classList.remove("hidden");
  }

  function hideModal() {
    modal?.classList.add("hidden");
  }

  function validateShippingForm() {
    if (!recipientNameInput.value.trim()) return "Vui lòng nhập người nhận.";
    if (!phoneInput.value.trim()) return "Vui lòng nhập số điện thoại.";
    if (!line1Input.value.trim()) return "Vui lòng nhập địa chỉ.";
    if (!cityInput.value.trim()) return "Vui lòng nhập thành phố.";
    if (!countryInput.value.trim()) return "Vui lòng nhập quốc gia.";
    if (!terms?.checked) return "Bạn cần đồng ý điều khoản trước khi đặt hàng.";
    return null;
  }

  async function initiateVNPayPayment(orderId) {
    const headers = {};
    if (csrfToken && csrfHeader) {
      headers[csrfHeader] = csrfToken;
    }

    const formData = new URLSearchParams();
    if (bankCodeInput?.value) {
      formData.set("bankCode", bankCodeInput.value);
    }
    formData.set("language", paymentLanguageInput?.value || "vn");

    const response = await fetch(`${ctx}/api/payments/vnpay/orders/${orderId}`, {
      method: "POST",
      headers,
      body: formData
    });

    const data = await response.json().catch(() => ({}));
    if (!response.ok) {
      throw new Error(data.message || "Khong the tao VNPay payment URL.");
    }
    if (!data.paymentUrl) {
      throw new Error("VNPay payment URL khong hop le.");
    }

    window.location.href = data.paymentUrl;
  }

  async function placeOrder() {
    clearError();

    const shippingMethod = currentShippingRadio()?.value;
    const paymentMethod = currentPaymentMethod();
    if (!shippingMethod) {
      showError("Vui lòng chọn phương thức vận chuyển.");
      return;
    }

    const validationMessage = validateShippingForm();
    if (validationMessage) {
      showError(validationMessage);
      return;
    }

    if (primarySubmit) {
      primarySubmit.disabled = true;
    }

    try {
      const headers = { "Content-Type": "application/json" };
      if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
      }

      const response = await fetch(`${ctx}/api/checkout`, {
        method: "POST",
        headers,
        body: JSON.stringify({
          paymentMethod,
          shippingMethod,
          addressId: addressSelect?.value ? Number(addressSelect.value) : null,
          recipientName: recipientNameInput.value.trim(),
          phone: phoneInput.value.trim(),
          line1: line1Input.value.trim(),
          line2: line2Input.value.trim(),
          city: cityInput.value.trim(),
          province: provinceInput.value.trim(),
          postalCode: postalCodeInput.value.trim(),
          country: countryInput.value.trim(),
          note: noteInput?.value?.trim() || null
        })
      });

      const data = await response.json().catch(() => ({}));
      if (!response.ok) {
        showError(data.message || "Không thể hoàn tất checkout.");
        return;
      }

      if (!data.paymentRequired) {
        if (!data.orderId) {
          showError("Checkout thành công nhưng không nhận được mã đơn hàng.");
          return;
        }
        window.location.href = `${ctx}/orders/${data.orderId}`;
        return;
      }

      if (paymentMethod !== "vnpay") {
        showError("He thong tra ve trang thai thanh toan khong phu hop.");
        return;
      }

      if (data.paymentUrl) {
        window.location.href = data.paymentUrl;
        return;
      }

      await initiateVNPayPayment(data.orderId);
    } catch (error) {
      showError("Không thể kết nối tới dịch vụ checkout. Vui lòng thử lại.");
    } finally {
      if (primarySubmit) {
        primarySubmit.disabled = false;
      }
    }
  }

  shippingRadios.forEach((radio) => radio.addEventListener("change", updateShippingSummary));
  paymentRadios.forEach((radio) => radio.addEventListener("change", switchPaymentMode));
  addressSelect?.addEventListener("change", updateSelectedAddressSummary);
  [recipientNameInput, phoneInput, line1Input, line2Input, cityInput, provinceInput, postalCodeInput, countryInput].forEach((input) => {
    input?.addEventListener("input", updateSelectedAddressSummary);
  });
  primarySubmit?.addEventListener("click", placeOrder);
  vnpayConfirm?.addEventListener("click", function () {
    hideModal();
    placeOrder();
  });
  modal?.addEventListener("click", function (event) {
    if (event.target === modal || event.target.classList.contains("modal-backdrop") || event.target.closest('[data-modal-hide="vnpay-modal"]')) {
      hideModal();
    }
  });

  updateShippingSummary();
  updateSelectedAddressSummary();
  switchPaymentMode();
})();
