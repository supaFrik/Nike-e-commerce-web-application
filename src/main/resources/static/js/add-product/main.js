// Main Application Module
function initializeApplication() {
    console.log('Initializing Product Management Application...');
    
    initializeState();
    prefillEditProduct();
    initializeDropdown();
    initializeColors();
    initializeSizes();
    initializeImages();
    initializeForm();
    initializeEventListeners();
    if(!window.EDIT_PRODUCT || !window.EDIT_PRODUCT.id){
        loadDraftIfAvailable();
    }

    console.log('Application initialized successfully!');
}

function initializeState() {
    if (window.AppState && typeof window.AppState.initialize === 'function') {
        console.log('Initializing AppState...');
        window.AppState.initialize();
        console.log('AppState initialized:', {
            availableColors: window.AppState.availableColors,
            currentColor: window.AppState.currentColor
        });
    } else {
        console.error('AppState not available or missing initialize method');
    }
}

function initializeDropdown() {
    if (window.Dropdown && typeof window.Dropdown.initialize === 'function') {
        window.Dropdown.initialize();
    }
}

function initializeColors() {
    if (window.ColorManager) {
        if (typeof window.ColorManager.initialize === 'function') {
            window.ColorManager.initialize();
        }
        if (typeof window.ColorManager.initializeListeners === 'function') {
            window.ColorManager.initializeListeners();
        }
    }
}

function initializeSizes() {
    if (window.SizeManager) {
        if (typeof window.SizeManager.initialize === 'function') {
            window.SizeManager.initialize();
        }
        if (typeof window.SizeManager.initializeListeners === 'function') {
            window.SizeManager.initializeListeners();
        }
    }
}

function initializeImages() {
    if (window.ImageManager) {
        if (typeof window.ImageManager.updateDisplay === 'function') {
            window.ImageManager.updateDisplay();
        }
        if (typeof window.ImageManager.initializeHandlers === 'function') {
            window.ImageManager.initializeHandlers();
        }
    }
}

function initializeForm() {
    if (window.FormHandler && typeof window.FormHandler.initialize === 'function') {
        window.FormHandler.initialize();
    }
}

function initializeEventListeners() {
    // Global keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        // Escape key closes all dropdowns
        if (e.key === 'Escape') {
            if (window.Dropdown && typeof window.Dropdown.closeAll === 'function') {
                window.Dropdown.closeAll();
            }
        }
        
        // Ctrl+Enter submits form
        if (e.ctrlKey && e.key === 'Enter') {
            const form = document.getElementById('productForm');
            if (form) {
                e.preventDefault();
                form.dispatchEvent(new Event('submit'));
            }
        }
    });
    
    document.addEventListener('visibilitychange', function() {
        if (document.hidden) {
            // Page is being hidden, could save state here
            console.log('Application state preserved');
        }
    });
    
    window.addEventListener('beforeunload', function(e) {
        const hasImages = Object.values(window.AppState?.colorImageData || {})
            .some(images => images.length > 0);
        
        if (hasImages) {
            e.preventDefault();
            e.returnValue = 'You have unsaved product data. Are you sure you want to leave?';
            return e.returnValue;
        }
    });
}

function refreshAllDisplays() {
    if (window.ColorManager && typeof window.ColorManager.updateOptions === 'function') {
        window.ColorManager.updateOptions();
    }
    if (window.SizeManager && typeof window.SizeManager.updateOptions === 'function') {
        window.SizeManager.updateOptions();
    }
    if (window.ImageManager && typeof window.ImageManager.updateDisplay === 'function') {
        window.ImageManager.updateDisplay();
    }
}

function handleApplicationError(error, context = 'Unknown') {
    console.error(`Application Error in ${context}:`, error);
    if (window.Toast && typeof window.Toast.show === 'function') {
        window.Toast.show(`An error occurred: ${error.message || 'Unknown error'}`);
    } else {
        alert(`An error occurred in ${context}: ${error.message || 'Unknown error'}`);
    }
}

window.addEventListener('error', function(e) {
    handleApplicationError(e.error, 'Global');
});

window.addEventListener('unhandledrejection', function(e) {
    handleApplicationError(e.reason, 'Promise');
    e.preventDefault();
});

function loadDraftIfAvailable(){
    try {
        const raw = localStorage.getItem('productDraft');
        if(!raw) return;
        const draft = JSON.parse(raw);
        if(!draft || !draft.name) return;
        const nameEl = document.getElementById('productName');
        const descEl = document.getElementById('productDescription');
        const priceEl = document.getElementById('productPrice');
        if(nameEl) nameEl.value = draft.name;
        if(descEl) descEl.value = draft.description || '';
        if(priceEl && draft.price){
            if(window.PriceFormatter){
                window.PriceFormatter.setRawValue(draft.price);
            } else {
                priceEl.value = draft.price; // fallback
            }
        }
        if(draft.type){
            const radio = document.getElementById(draft.type.toUpperCase());
            if(radio) radio.checked = true;
        }
        if(draft.categoryId){
            const hid = document.getElementById('categoryIdHidden');
            if(hid) hid.value = draft.categoryId;
            // attempt to find option text
            const opt = document.querySelector(`#dropdown-options .dropdown-option[data-id='${draft.categoryId}']`);
            if(opt){
                const span = document.getElementById('selected-category');
                if(span) span.textContent = opt.textContent.trim();
                if(window.AppState) window.AppState.setSelectedCategory(opt.textContent.trim());
            }
        }
        // Colors
        if(Array.isArray(draft.colors) && draft.colors.length){
            if(window.AppState){
                window.AppState.availableColors = [];
                window.AppState.colorImageData = {};
                window.AppState.colorSizeData = {};
                window.AppState.defaultImageData = {};
                draft.colors.forEach(c => {
                    if(!c || !c.name) return;
                    window.AppState.availableColors.push(c.name);
                    window.AppState.colorSizeData[c.name] = c.sizes && c.sizes.length ? c.sizes : ['40'];
                    const imgs = (c.images || []).map((src,idx)=>({src, name: 'draft_img_'+idx, id: Date.now()+Math.random()+idx, color: c.name}));
                    window.AppState.colorImageData[c.name] = imgs;
                    const di = (typeof c.defaultImageIndex === 'number' && c.defaultImageIndex >=0 && c.defaultImageIndex < imgs.length) ? imgs[c.defaultImageIndex].id : (imgs[0]?.id);
                    if(di) window.AppState.defaultImageData[c.name] = di;
                });
                window.AppState.setCurrentColor(window.AppState.availableColors[0]);
            }
            // Refresh UI modules if present
            if(window.ColorManager) window.ColorManager.updateOptions();
            if(window.SizeManager) window.SizeManager.updateOptions();
            if(window.ImageManager) window.ImageManager.updateDisplay();
        }
        console.log('Draft restored');
    } catch(e){ console.warn('Failed to load draft', e); }
}

function prefillEditProduct(){
    if(!window.EDIT_PRODUCT || !window.EDIT_PRODUCT.id){ return; }
    try {
        console.log('[EditMode] Prefilling product data', window.EDIT_PRODUCT.id);
        const p = window.EDIT_PRODUCT;
        // Basic fields
        const nameEl = document.getElementById('productName');
        const descEl = document.getElementById('productDescription');
        const priceEl = document.getElementById('productPrice');
        if(nameEl) nameEl.value = p.name || '';
        if(descEl) descEl.value = p.description || '';
        if(priceEl && p.price != null){
            if(window.PriceFormatter){ window.PriceFormatter.setRawValue(p.price); }
            else { priceEl.value = p.price; }
        }
        if(p.type){
            const radio = document.getElementById(p.type.toUpperCase());
            if(radio) radio.checked = true;
        }
        if(p.categoryId){
            const hid = document.getElementById('categoryIdHidden');
            if(hid) hid.value = p.categoryId;
            const opt = document.querySelector(`#dropdown-options .dropdown-option[data-id='${p.categoryId}']`);
            if(opt){
                const span = document.getElementById('selected-category');
                if(span) span.textContent = opt.textContent.trim();
                if(window.AppState) window.AppState.setSelectedCategory(opt.textContent.trim());
            }
        }
        // Colors, sizes, images
        if(Array.isArray(p.colors) && p.colors.length){
            window.AppState.availableColors = [];
            window.AppState.colorImageData = {};
            window.AppState.colorSizeData = {};
            window.AppState.defaultImageData = {};
            window.AppState.colorSizeStockData = {};
            p.colors.forEach((cObj)=>{
                if(!cObj || !cObj.name) return;
                window.AppState.availableColors.push(cObj.name);
                const sizes = Array.isArray(cObj.sizes) && cObj.sizes.length? cObj.sizes : ['40'];
                window.AppState.colorSizeData[cObj.name] = sizes;
                // stocks
                const stockMap = {};
                if(Array.isArray(cObj.sizeStocks)){
                    sizes.forEach((sz, idx)=>{ stockMap[sz] = (cObj.sizeStocks[idx] != null ? cObj.sizeStocks[idx] : 0); });
                } else {
                    sizes.forEach(sz=> stockMap[sz] = 0);
                }
                window.AppState.colorSizeStockData[cObj.name] = stockMap;
                // images
                const imgs = (cObj.images || []).map((src, idx)=> ({
                    src: src,
                    name: 'img_'+idx,
                    id: Date.now() + Math.random() + idx,
                    color: cObj.name
                }));
                window.AppState.colorImageData[cObj.name] = imgs;
                // default image
                let defIdx = 0;
                if(typeof cObj.defaultImageIndex === 'number' && cObj.defaultImageIndex >=0 && cObj.defaultImageIndex < imgs.length){ defIdx = cObj.defaultImageIndex; }
                if(imgs[defIdx]) window.AppState.defaultImageData[cObj.name] = imgs[defIdx].id;
            });
            window.AppState.setCurrentColor(window.AppState.availableColors[0]);
        }
        // Refresh modules
        if(window.ColorManager) window.ColorManager.updateOptions && window.ColorManager.updateOptions();
        if(window.SizeManager) window.SizeManager.updateOptions && window.SizeManager.updateOptions();
        if(window.ImageManager) window.ImageManager.updateDisplay && window.ImageManager.updateDisplay();
    } catch(err){ console.error('[EditMode] Prefill failed', err); }
}

window.App = {
    initialize: initializeApplication,
    refresh: refreshAllDisplays,
    handleError: handleApplicationError
};

document.addEventListener('DOMContentLoaded', function() {
    try {
        setTimeout(() => {
            initializeApplication();
        }, 100);
    } catch (error) {
        handleApplicationError(error, 'Initialization');
    }
});