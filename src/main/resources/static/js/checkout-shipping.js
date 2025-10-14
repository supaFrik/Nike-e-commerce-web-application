(function(){
    function initShippingMethodModule(){
        const radios = document.querySelectorAll('input[name="shippingMethod"]');
        if(!radios.length) return; // nothing to do
        const shippingHidden = document.getElementById('shippingMethodHidden');
        const subtotalInput = document.getElementById('subtotalValue');
        const discountInput = document.getElementById('discountValue');
        const subtotalVal = parseFloat(subtotalInput ? subtotalInput.value : '0') || 0;
        const discountVal = parseFloat(discountInput ? discountInput.value : '0') || 0;
        const shippingDisplay = document.getElementById('shippingCostDisplay');
        const totalDisplay = document.getElementById('totalDisplay');
        const formatter = new Intl.NumberFormat('vi-VN',{style:'currency',currency:'VND'});

        function updateSelectedVisual(selectedRadio){
            document.querySelectorAll('.delivery-option').forEach(d => d.classList.remove('selected'));
            if(selectedRadio){
                const parent = selectedRadio.closest('.delivery-option');
                if(parent){ parent.classList.add('selected'); }
            }
        }

        function recalc(){
            const selected = document.querySelector('input[name="shippingMethod"]:checked');
            if(!selected) return;
            const shipping = parseFloat(selected.dataset.cost || '0') || 0;
            const total = subtotalVal + shipping - discountVal;
            if (shippingDisplay) shippingDisplay.textContent = formatter.format(shipping);
            if (totalDisplay) totalDisplay.textContent = formatter.format(total);
            if (shippingHidden) shippingHidden.value = selected.value;
            updateSelectedVisual(selected);
        }

        radios.forEach(r => r.addEventListener('change', recalc));
        // Make whole label clickable / id already associates, but ensure we recalc early
        document.querySelectorAll('.delivery-option label').forEach(lbl => {
            lbl.addEventListener('click', () => {
                // Delay to allow default radio change if triggered by native click
                setTimeout(recalc, 0);
            });
        });
        recalc();
    }

    if(document.readyState === 'loading'){
        document.addEventListener('DOMContentLoaded', initShippingMethodModule);
    } else {
        initShippingMethodModule();
    }
})();
