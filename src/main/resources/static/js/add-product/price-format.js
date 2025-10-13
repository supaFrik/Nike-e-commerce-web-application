(function(){
    const nf = new Intl.NumberFormat('vi-VN'); // plain grouping formatter

    function formatVND(value){
        if(value == null || value === '') return '';
        const num = parseInt(value,10);
        if(isNaN(num)) return '';
        return nf.format(num) + ' ₫';
    }

    function extractDigits(str){
        if(!str) return '';
        return (str+ '').replace(/[^0-9]/g,'');
    }

    function applyFormattedDisplay(input){
        const raw = input.dataset.raw;
        if(raw && raw.length){
            input.value = formatVND(raw);
        } else {
            input.value = '';
        }
    }

    function revertToRaw(input){
        const raw = input.dataset.raw || '';
        input.value = raw;
    }

    function attachPriceHandlers(){
        const input = document.getElementById('productPrice');
        if(!input) return;
        // Initialize dataset raw if absent
        if(!input.dataset.raw && input.value){
            const digits = extractDigits(input.value);
            if(digits){ input.dataset.raw = ''+parseInt(digits,10); }
        }
        applyFormattedDisplay(input);

        input.addEventListener('focus', () => {
            revertToRaw(input);
            // Place cursor at end
            setTimeout(()=>{ try { const len = input.value.length; input.setSelectionRange(len,len);}catch(e){} },0);
        });

        input.addEventListener('blur', () => {
            // Normalize raw digits on blur
            const digits = extractDigits(input.value);
            if(digits){ input.dataset.raw = ''+parseInt(digits,10); }
            applyFormattedDisplay(input);
        });

        input.addEventListener('input', () => {
            const digits = extractDigits(input.value);
            if(!digits){
                input.dataset.raw = '';
                input.value = '';
                return;
            }
            input.dataset.raw = ''+parseInt(digits,10);
            // Show raw while typing for easier editing
            input.value = digits;
        });

        // Optional: handle paste for cleanliness
        input.addEventListener('paste', (e)=>{
            e.preventDefault();
            const text = (e.clipboardData || window.clipboardData).getData('text');
            const digits = extractDigits(text);
            if(digits){
                input.dataset.raw = ''+parseInt(digits,10);
                input.value = digits;
            }
        });
    }

    window.PriceFormatter = {
        formatExisting: function(){
            const input = document.getElementById('productPrice');
            if(input){ applyFormattedDisplay(input); }
        },
        setRawValue: function(val){
            const input = document.getElementById('productPrice');
            if(!input) return;
            if(val == null || val === ''){ delete input.dataset.raw; input.value=''; return; }
            const digits = extractDigits(val);
            if(digits){ input.dataset.raw = ''+parseInt(digits,10); applyFormattedDisplay(input); }
        },
        getRawValue: function(){
            const input = document.getElementById('productPrice');
            if(!input) return null;
            const raw = input.dataset.raw;
            if(!raw) return null;
            const n = parseInt(raw,10);
            return isNaN(n)? null : n;
        }
    };

    document.addEventListener('DOMContentLoaded', attachPriceHandlers);
})();

