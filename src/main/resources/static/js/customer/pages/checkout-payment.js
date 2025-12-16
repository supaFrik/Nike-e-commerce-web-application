// Simple validation & modal trigger for VNPay QR
(function() {
  const pmCard = document.getElementById('pm-card');
  const pmVNPay = document.getElementById('pm-vnpay');
  const pmNapas = document.getElementById('pm-napas');
  const pmCOD = document.getElementById('pm-cod');
  const cardFields = document.getElementById('card-fields');
  const vnpayFields = document.getElementById('vnpay-fields');
  const primarySubmit = document.getElementById('primary-submit');
  const cardBrands = document.getElementById('card-brands');
  const modal = document.getElementById('vnpay-modal');
  const orderIdEl = document.getElementById('order-id');
  const orderAmtEl = document.getElementById('order-amount');
  const qrImgEl = document.getElementById('qr-image');
  const shippingForm = document.getElementById('completeCheckoutForm');
  const paymentHidden = document.getElementById('paymentMethodHidden');
  const vnpayConfirmBtn = document.getElementById('vnpay-confirm');

  function showModal() { modal.classList.remove('hidden'); }
  function hideModal() { modal.classList.add('hidden'); }
  modal?.addEventListener('click', (e) => {
    if (e.target === modal || e.target.classList.contains('modal-backdrop') || e.target.closest('[data-modal-hide="vnpay-modal"]')) hideModal();
  });

  function setPrimary(label) {
    if (!primarySubmit) return;
    primarySubmit.classList.remove('hidden');
    primarySubmit.textContent = label;
  }

  function currentPaymentMethod() {
    if (pmVNPay?.checked) return 'vnpay';
    if (pmNapas?.checked) return 'napas';
    if (pmCOD?.checked) return 'cod';
    return 'card';
  }

  function switchTo(mode) {
    const err = document.getElementById('bank-error');
    if (err) err.classList.add('hidden');
    cardFields?.classList.add('hidden');
    vnpayFields?.classList.add('hidden');
    cardBrands?.classList.add('hidden');
    primarySubmit?.classList.add('hidden');

    if (mode === 'card') {
      cardFields?.classList.remove('hidden');
      cardBrands?.classList.remove('hidden');
      setPrimary('Hoàn tất thanh toán');
    } else if (mode === 'vnpay') {
      vnpayFields?.classList.remove('hidden');
      setPrimary('Thanh toán bằng VNPay QR');
    } else if (mode === 'napas') {
      setPrimary('Hoàn tất thanh toán');
    } else if (mode === 'cod') {
      setPrimary('Hoàn tất thanh toán');
    }
  }

  pmCard?.addEventListener('change', () => switchTo('card'));
  pmVNPay?.addEventListener('change', () => switchTo('vnpay'));
  pmNapas?.addEventListener('change', () => switchTo('napas'));
  pmCOD?.addEventListener('change', () => switchTo('cod'));

  function validateTerms() {
    const terms = document.getElementById('terms');
    if (terms && !terms.checked) {
      // trigger existing validation message by firing submit on shipping form with preventDefault
      const ev = new Event('submit', { cancelable: true });
      if (shippingForm?.dispatchEvent(ev) === false) {
        // nothing
      }
      return false;
    }
    return true;
  }

  function submitShippingForm() {
    if (!shippingForm) return;
    const method = currentPaymentMethod();
    if (paymentHidden) paymentHidden.value = method;
    shippingForm.submit();
  }

  // Primary submit handler
  primarySubmit?.addEventListener('click', (e) => {
    const method = currentPaymentMethod();
    // always prevent default and decide
    e.preventDefault();

    // basic terms check
    if (!validateTerms()) return;

    if (method === 'vnpay') {
      // Validate VNPay fields before showing modal
      const name = (document.getElementById('vnpay-full-name')?.value || document.getElementById('full_name')?.value || '').trim();
      const bank = document.getElementById('bank-name')?.value;
      const account = (document.getElementById('bank-account')?.value || '').replace(/\s+/g, '');
      const isValid = name.length >= 3 && bank && /^[0-9]{10,19}$/.test(account);

      if (!isValid) {
        let err = document.getElementById('bank-error');
        if (!err) {
          const div = document.createElement('div');
          div.id = 'bank-error';
          div.className = 'alert alert-error';
          div.innerHTML = '<strong>Tài khoản ngân hàng không hợp lệ.</strong> Vui lòng kiểm tra lại.';
          vnpayFields?.appendChild(div);
        } else {
          err.classList.remove('hidden');
        }
        return;
      }

      const err2 = document.getElementById('bank-error');
      if (err2) err2.classList.add('hidden');

      // Build fake QR data; in real case, fetch from server
      const orderId = '#100204';
      const amountEl = document.getElementById('totalDisplay');
      const amountText = amountEl ? amountEl.textContent : '0 VND';
      orderIdEl.textContent = `Thanh toán mã đơn hàng ${orderId}`;
      orderAmtEl.textContent = amountText || '0 VND';

      const amountNumeric = (amountText || '').replace(/[^0-9]/g,'');
      const qrData = encodeURIComponent(`VNPAYQR|name=${name}|bank=${bank}|account=${account}|amount=${amountNumeric}|order=${orderId}`);
      if (qrImgEl) qrImgEl.src = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${qrData}`;

      showModal();
    } else {
      // Card/Napas/COD: submit immediately
      submitShippingForm();
    }
  });

  // VNPay confirm proceeds to submit
  vnpayConfirmBtn?.addEventListener('click', () => {
    hideModal();
    submitShippingForm();
  });

  // Initialize default state
  switchTo('card');
})();
