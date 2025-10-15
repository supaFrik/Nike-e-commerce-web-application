(function(){
  function sanitizePriceInput(val){ if(!val) return null; const digits=(val+'').replace(/[^0-9]/g,''); if(!digits) return null; return parseInt(digits,10); }
  function showToast(msg){ if(window.Toast && typeof window.Toast.show==='function'){ window.Toast.show(msg); } else { console.log('[Toast]', msg); } }
  function collectPayload(){
    const name = document.getElementById('productName')?.value?.trim();
    const description = document.getElementById('productDescription')?.value?.trim();
    const priceRaw = (window.PriceFormatter && window.PriceFormatter.getRawValue()) || sanitizePriceInput(document.getElementById('productPrice')?.value);
    const salePriceRaw = sanitizePriceInput(document.getElementById('productSalePrice')?.value);
    const categoryIdVal = document.getElementById('categoryIdHidden')?.value || '';
    const categoryId = categoryIdVal ? parseInt(categoryIdVal,10) : NaN;
    const type = document.querySelector('input[name="gender"]:checked')?.id?.toUpperCase() || 'UNISEX';
    const stockVal = sanitizePriceInput(document.getElementById('productStock')?.value);
    const payload = {};
    if(name) payload.name = name; else showToast('Name required');
    payload.description = description || '';
    if(priceRaw!=null) payload.price = priceRaw; else showToast('Price required');
    if(salePriceRaw!=null) payload.salePrice = salePriceRaw; // optional
    if(!isNaN(categoryId)) payload.categoryId = categoryId; else showToast('Select category');
    if(type) payload.type = type;
    if(stockVal!=null) payload.stock = stockVal;
    return payload;
  }
  async function doUpdate(){
    const idEl = document.getElementById('productId');
    const id = idEl ? idEl.value : null;
    if(!id){ showToast('Missing product id'); return; }
    const payload = collectPayload();
    if(!payload.name || !payload.price || !payload.categoryId){ return; }
    const ctx=(window.APP_CTX||'').replace(/\/$/,'');
    const tokenMeta=document.querySelector('meta[name="_csrf"]');
    const headerMeta=document.querySelector('meta[name="_csrf_header"]');
    const csrfToken=tokenMeta?tokenMeta.getAttribute('content'):null;
    const csrfHeader=headerMeta?headerMeta.getAttribute('content'):null;
    const headers={'Content-Type':'application/json'}; if(csrfToken && csrfHeader){ headers[csrfHeader]=csrfToken; }
    try {
      showToast('Saving...');
      const res = await fetch(ctx + '/admin/api/products/' + id, { method:'PUT', headers, body: JSON.stringify(payload) });
      const data = await res.json().catch(()=>({}));
      if(!res.ok) throw new Error(data.error || data.message || 'Update failed');
      showToast('Product updated successfully');
      setTimeout(()=>{ window.location.href = ctx + '/admin/product/list'; }, 600);
    } catch(err){ console.error('Update failed', err); showToast('Error: ' + err.message); }
  }
  function setupCategoryDropdown(){
    const opts=document.getElementById('dropdown-options'); if(!opts) return;
    opts.querySelectorAll('.dropdown-option').forEach(opt=>{
      opt.addEventListener('click', function(){
        const id=this.getAttribute('data-id');
        const text=this.textContent.trim();
        const hidden=document.getElementById('categoryIdHidden');
        const span=document.getElementById('selected-category');
        if(hidden) hidden.value=id; if(span) span.textContent=text;
        if(window.AppState) window.AppState.setSelectedCategory(text);
        if(window.Dropdown && typeof window.Dropdown.closeAll==='function'){ window.Dropdown.closeAll(); }
      });
    });
  }
  function syncInitialCategory(){
    const catName = document.getElementById('selected-category')?.textContent?.trim();
    if(catName && catName !== 'Select Category' && window.AppState){ window.AppState.setSelectedCategory(catName); }
  }
  function attachEvents(){
    const form=document.getElementById('editProductForm');
    if(form){ form.addEventListener('submit', e=>{ e.preventDefault(); doUpdate(); }); }
    document.addEventListener('keydown', e=>{ if(e.ctrlKey && e.key==='Enter'){ e.preventDefault(); doUpdate(); } });
  }
  function init(){ attachEvents(); setupCategoryDropdown(); syncInitialCategory(); }
  document.addEventListener('DOMContentLoaded', init);
})();

