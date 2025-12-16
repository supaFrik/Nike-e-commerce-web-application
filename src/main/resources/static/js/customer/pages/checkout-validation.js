(function(){
    document.addEventListener('DOMContentLoaded', () => {
        const form = document.getElementById('completeCheckoutForm');
        if(!form) return;

        const fieldLabels = {
            firstName: 'First name',
            lastName: 'Last name',
            email: 'Email address',
            phone: 'Phone number',
            country: 'Country',
            city: 'City',
            address: 'Street address'
        };

        form.addEventListener('submit', (e) => {
            if(!validateCheckoutForm()) {
                e.preventDefault();
            }
        });

        function validateCheckoutForm(){
            const requiredFields = Object.keys(fieldLabels);
            let isValid = true;
            const messages = [];
            const errorClass = 'input-error';

            requiredFields.forEach(id => {
                const el = document.getElementById(id);
                if(el) {
                    el.classList.remove(errorClass);
                    removeInlineError(el);
                }
            });
            const terms = document.getElementById('terms');
            if(terms) removeInlineError(terms);

            requiredFields.forEach(id => {
                const el = document.getElementById(id);
                if(!el) return;
                if(!el.value || !el.value.trim()) {
                    isValid = false;
                    messages.push(`${fieldLabels[id]} is required.`);
                    markError(el, `${fieldLabels[id]} is required.`);
                }
            });

            // Email
            const emailEl = document.getElementById('email');
            if(emailEl && emailEl.value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailEl.value.trim())) {
                isValid = false;
                messages.push('Please enter a valid email address.');
                markError(emailEl, 'Invalid email');
            }

            // Sđt độ dài từ 9-12 số
            const phoneEl = document.getElementById('phone');
            if(phoneEl && phoneEl.value && !/^[0-9]{9,12}$/.test(phoneEl.value.trim())) {
                isValid = false;
                messages.push('Phone number must contain 9–12 digits.');
                markError(phoneEl, '9–12 digits');
            }

            // Kiểm tra shipping method đã chọn
            const shippingSelected = document.querySelector('input[name="shippingMethod"]:checked');
            if(!shippingSelected){
                isValid = false;
                messages.push('Please select a shipping method.');
                const container = document.querySelector('.delivery-options');
                if(container) container.classList.add('input-error');
            } else {
                const container = document.querySelector('.delivery-options');
                if(container) container.classList.remove('input-error');
            }

            // Terms
            if(terms && !terms.checked) {
                isValid = false;
                messages.push('You must agree to the Terms and Conditions.');
                markError(terms, 'Required');
            }

            if(!isValid) {
                const firstErr = document.querySelector('.' + errorClass);
                if(firstErr && typeof firstErr.scrollIntoView === 'function') {
                    firstErr.scrollIntoView({behavior:'smooth', block:'center'});
                }
            }
            return isValid;
        }

        function markError(el, msg){
            el.classList.add('input-error');
            if(!el.parentElement) return;
            let span = document.createElement('div');
            span.className = 'field-error-msg';
            span.textContent = msg;
            el.parentElement.appendChild(span);
        }

        function removeInlineError(el){
            if(!el.parentElement) return;
            const existing = el.parentElement.querySelector('.field-error-msg');
            if(existing) existing.remove();
        }
    });
})();
