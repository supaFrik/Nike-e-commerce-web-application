// Form Handler Module
// Handles form submission and validation

// Form submission
function initializeFormHandler() {
    const productForm = document.getElementById('productForm');
    if (!productForm) return;
    
    productForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        let hasImages = Object.values(window.AppState.colorImageData).some(arr => arr.length>0);
        if(!hasImages){ window.Toast.show('Please upload at least one image'); return; }
        if(!validateFormFields()) return;
        const payload = collectFormData();
        submitToBackend(payload);
    });
}

// Validate form fields
function validateFormFields() {
    const requiredFields = [
        { id: 'productName', message: 'Please enter a product name' },
        { id: 'productDescription', message: 'Please enter a product description' },
        { id: 'productPrice', message: 'Please enter a product price' }
    ];
    
    for (const field of requiredFields) {
        const element = document.getElementById(field.id);
        if (element && !element.value.trim()) {
            window.Toast.show(field.message);
            element.focus();
            return false;
        }
    }
    
    // Validate category selection
    if (!window.AppState.selectedCategory || window.AppState.selectedCategory === 'Select Category') {
        window.Toast.show('Please select a product category');
        return false;
    }
    
    // Validate price is a positive number
    const priceElement = document.getElementById('productPrice');
    if (priceElement) {
        const raw = priceElement.dataset.raw || priceElement.value;
        const price = sanitizePriceInput(raw);
        if (price === null || price <= 0) {
            window.Toast.show('Please enter a valid price');
            priceElement.focus();
            return false;
        }
    }
    
    return true;
}

// Submit to backend API
function submitToBackend(formData){
    const ctx = (window.APP_CTX || '').replace(/\/$/, '');
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    const csrfToken = tokenMeta ? tokenMeta.getAttribute('content') : null;
    const csrfHeader = headerMeta ? headerMeta.getAttribute('content') : null;

    const headers = { 'Content-Type': 'application/json' };
    if(csrfToken && csrfHeader){
        headers[csrfHeader] = csrfToken;
    }

    fetch(ctx + '/admin/api/products', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(formData)
    }).then(async res => {
        const data = await res.json().catch(()=>({}));
        if(!res.ok){
            throw new Error(data.error || data.message || 'Server error');
        }
        // SUCCESS --------------------------------------------------
        try { localStorage.removeItem('productDraft'); } catch(e) {  }
        // Reset state
        if(window.FormHandler && typeof window.FormHandler.reset === 'function') {
            window.FormHandler.reset();
        } else if(window.AppState && typeof window.AppState.reset === 'function') {
            window.AppState.reset();
        }
        window.Toast && window.Toast.show('Product created successfully');
        const urlParams = new URLSearchParams(window.location.search);
        const stayMode = urlParams.has('stay') || window.location.hash === '#stay';
        if(stayMode){
            return;
        }
        setTimeout(()=>{ window.location.href = ctx + (data.redirectUrl || '/admin/product/list'); }, 600);
    }).catch(err => {
        console.error('Create product failed', err);
        window.Toast && window.Toast.show('Create failed: '+ err.message);
    });
}

// Collect all form data
function collectFormData() {
    const priceField = document.getElementById('productPrice');
    const priceValue = sanitizePriceInput(priceField ? (priceField.dataset.raw || priceField.value) : null) || 0;
    const base = {
        name: document.getElementById('productName')?.value || '',
        description: document.getElementById('productDescription')?.value || '',
        price: priceValue,
        salePrice: null,
        type: document.querySelector('input[name="gender"]:checked')?.id?.toUpperCase() || 'UNISEX',
        categoryId: parseInt(document.getElementById('categoryIdHidden')?.value || 0) || null,
        seo: null
    };
    const stockInput = document.getElementById('productStock');
    if (stockInput) {
        const ds = parseInt(stockInput.value, 10);
        if (!isNaN(ds) && ds >= 0) base.defaultStock = ds;
    }
    const colors = (window.AppState.availableColors || []).map(color => {
        const imgs = (window.AppState.colorImageData[color] || []).map(i => i.src);
        const defaultId = window.AppState.defaultImageData[color];
        let defaultIdx = -1;
        if(defaultId){
            const arr = window.AppState.colorImageData[color] || [];
            defaultIdx = arr.findIndex(i=>i.id === defaultId);
        }
        const sizesArr = window.AppState.colorSizeData[color] || ['40'];
        const stockMap = (window.AppState.colorSizeStockData && window.AppState.colorSizeStockData[color]) || {};
        const sizeStocks = sizesArr.map(sz => {
            const v = parseInt(stockMap[sz], 10);
            if (isNaN(v) || v < 0) return (base.defaultStock != null ? base.defaultStock : 0);
            return v;
        });
        const colorObj = {
            name: color,
            sizes: sizesArr,
            sizeStocks: sizeStocks,
            images: imgs,
            defaultImageIndex: defaultIdx >=0 ? defaultIdx : 0
        };
        if (base.defaultStock != null) {
            colorObj.defaultStock = base.defaultStock;
        }
        return colorObj;
    });
    base.colors = colors;
    base.totalImages = colors.reduce((t,c)=> t + c.images.length,0);
    return base;
}

// Reset form to initial state
function resetForm() {
    const form = document.getElementById('productForm');
    if (form) {
        form.reset();
    }
    
    // Reset state
    window.AppState.reset();
    
    // Reinitialize components
    if (window.ColorManager) {
        window.ColorManager.initialize();
    }
    if (window.SizeManager) {
        window.SizeManager.initialize();
    }
    if (window.ImageManager) {
        window.ImageManager.updateDisplay();
    }
    if (window.Dropdown) {
        window.Dropdown.closeAll();
    }
}

// Optional: Save draft locally (placeholder)
function saveDraft(){
    try {
        const draft = collectFormData();
        localStorage.setItem('productDraft', JSON.stringify(draft));
        window.Toast.show('Draft saved locally');
    } catch(e){ console.warn('Save draft failed', e); }
}

window.addEventListener('DOMContentLoaded', () => {
    const draftBtn = document.getElementById('saveDraftBtn');
    if(draftBtn){ draftBtn.addEventListener('click', saveDraft); }

    // Check tên
    const nameInput = document.getElementById('productName');
    const conflictBox = document.getElementById('productNameConflict');
    let nameTimer = null;
    function hideConflict(){ if(conflictBox){ conflictBox.style.display='none'; conflictBox.innerHTML=''; conflictBox.dataset.conflict='false'; } }
    async function checkNameInstant(value){
        const trimmed = (value||'').trim();
        if(!trimmed || trimmed.length < 3){ hideConflict(); return; }
        try {
            const ctx = (window.APP_CTX || '').replace(/\/$/, '');
            const resp = await fetch(ctx + '/admin/api/products/check-name?name=' + encodeURIComponent(trimmed));
            if(!resp.ok) return; // silent
            const data = await resp.json();
            if(!data) return;
            if(data.conflict){
                if(conflictBox){
                    const suggestionSafe = data.suggestion ? data.suggestion.replace(/</g,'&lt;') : '';
                    conflictBox.innerHTML = '<strong>Exact product name already exists.</strong>' + (suggestionSafe? ' Try: <a href="#" id="applyNameSuggestion" style="font-weight:600;">'+suggestionSafe+'</a>' : '') + '<br><span style="opacity:.8;">(Normalized: '+ (data.normalized||'') +')</span>';
                    conflictBox.style.display='block';
                    conflictBox.dataset.conflict='true';
                    const a = document.getElementById('applyNameSuggestion');
                    if(a){
                        a.addEventListener('click', (e)=>{ e.preventDefault(); if(nameInput && data.suggestion){ nameInput.value = data.suggestion; hideConflict(); nameInput.focus(); } });
                    }
                }
            } else {
                hideConflict();
            }
        } catch(err){ /* ignore transient */ }
    }
    if(nameInput){
        nameInput.addEventListener('blur', ()=> checkNameInstant(nameInput.value));
        nameInput.addEventListener('input', ()=> { // debounce while typing
            if(nameTimer) clearTimeout(nameTimer);
            nameTimer = setTimeout(()=> checkNameInstant(nameInput.value), 500);
        });
    }
});

// Export for global access
window.FormHandler = {
    initialize: initializeFormHandler,
    validate: validateFormFields,
    collectData: collectFormData,
    reset: resetForm
};

function sanitizePriceInput(val){
    if(!val) return null;
    const digits = val.toString().replace(/[^0-9]/g,'');
    if(!digits) return null;
    return parseInt(digits,10);
}
