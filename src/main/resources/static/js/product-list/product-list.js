/* Product List Page Script (server-wired) */
(function(){
  const state = {
    items: [],
    page: 1,
    pageSize: 20,
    totalItems: 0,
    totalPages: 1,
    sort: 'name',
    category: 'all',
    search: ''
  };
  const els = {};
  const SELECTORS = { grid:'#productGrid', empty:'#productGridEmpty', paginationList:'#paginationList', paginationSummary:'#paginationSummary', search:'#search', sortToggle:'#sortToggle', sortMenu:'#sortMenu', categoryTabs:'#categoryTabs', cardTemplate:'#productCardTemplate', editPanel:'.edit-panel', editForm:'#editForm', editTabs:'.edit-tab', editSections:'.edit-section', editClose:'#closeEdit', editImage:'#editImage', editCategorySelect:'#editCategorySelect' };
  let abortCtrl = null;

  function qs(sel,root=document){ return root.querySelector(sel); }
  function qsa(sel,root=document){ return Array.from(root.querySelectorAll(sel)); }
  function val(el){ return el && 'value' in el ? el.value : null; }
  function ctx(){ return (window.APP_CTX||'').replace(/\/$/,''); }
  function csrf(){ const t=qs('meta[name="_csrf"]'); const h=qs('meta[name="_csrf_header"]'); return { token:t&&t.content, header:h&&h.content }; }

  function init(){ cacheEls(); bindEvents(); dynamicCategoriesIntoTabs(); fetchProducts(); buildCategorySelect(); }

  function cacheEls(){ Object.entries(SELECTORS).forEach(([k,sel])=> els[k]=qs(sel)); els.editTabs=qsa(SELECTORS.editTabs); els.editSections=qsa(SELECTORS.editSections); }

  function bindEvents(){ if(els.search) els.search.addEventListener('input', debounce(()=>{ state.search = els.search.value.trim(); state.page=1; fetchProducts(); },300)); if(els.sortToggle) els.sortToggle.addEventListener('click', toggleSortMenu); if(els.sortMenu) els.sortMenu.addEventListener('click', onSortSelect); if(els.categoryTabs) els.categoryTabs.addEventListener('click', onCategoryTab); if(els.paginationList) els.paginationList.addEventListener('click', onPaginationClick); els.editTabs.forEach(t=>t.addEventListener('click', switchEditTab)); if(els.editClose) els.editClose.addEventListener('click', closeEditor); if(els.editForm) els.editForm.addEventListener('submit', onEditSubmit); const discard=document.getElementById('discardBtn'); if(discard) discard.addEventListener('click', onDiscard); const addBtn=document.getElementById('addProductBtn'); if(addBtn) addBtn.addEventListener('click', ()=> { window.location.href = ctx() + '/admin/product/add'; }); }

  function dynamicCategoriesIntoTabs(){ if(!window.__CATEGORIES__ || !els.categoryTabs) return; // keep existing static ones but could append dynamic

    window.__CATEGORIES__.forEach(c=>{ const key=c.key; if(!els.categoryTabs.querySelector(`[data-filter="${key}"]`)){ const btn=document.createElement('button'); btn.className='tab'; btn.type='button'; btn.dataset.filter=key; btn.role='tab'; btn.textContent=c.name; btn.setAttribute('aria-selected','false'); els.categoryTabs.appendChild(btn); } }); }

  function buildQuery(){ const p=new URLSearchParams(); p.set('page', state.page); p.set('size', state.pageSize); p.set('sort', state.sort); if(state.search) p.set('keyword', state.search); if(state.category && state.category!=='all'){ const cat=categoryByKey(state.category); if(cat) p.set('categoryId', cat.id); } return p.toString(); }

  function fetchProducts(){ if(abortCtrl) abortCtrl.abort(); abortCtrl = new AbortController(); setBusy(true); const url = ctx() + '/admin/api/products?' + buildQuery(); fetch(url,{ signal: abortCtrl.signal }).then(r=> r.ok? r.json(): Promise.reject(r)).then(applyResponse).catch(err=>{ if(err.name==='AbortError') return; console.error('Fetch products failed', err); showEmpty('Failed to load products'); }).finally(()=> setBusy(false)); }

  function applyResponse(data){ state.items = Array.isArray(data.items)? data.items: []; state.totalItems = data.totalItems||0; state.totalPages = data.totalPages||1; state.page = data.page||1; renderGrid(); renderPagination(); }

  function setBusy(flag){ if(els.grid) els.grid.setAttribute('aria-busy', flag? 'true':'false'); }

  function renderGrid(){ if(!els.grid) return; els.grid.innerHTML=''; if(!state.items.length){ showEmpty(); return; } hideEmpty(); const frag=document.createDocumentFragment(); state.items.forEach(p=> frag.appendChild(buildCard(p))); els.grid.appendChild(frag); }
  function showEmpty(msg){ if(els.empty){ els.empty.textContent = msg||'No products found.'; els.empty.style.display='block'; } if(els.grid) els.grid.innerHTML=''; }
  function hideEmpty(){ if(els.empty) els.empty.style.display='none'; }

  function buildCard(p){ const template = els.cardTemplate?.content?.firstElementChild; const node = template? template.cloneNode(true): document.createElement('article'); node.classList.add('product-card'); const thumb=node.querySelector('[data-ref="thumb"]')||node.querySelector('.thumb'); const img=node.querySelector('[data-ref="image"]'); const hasImage = !!(p.imageUrl && p.imageUrl.trim()); if(img){ if(hasImage){ img.src = ctx()+p.imageUrl; img.alt = p.name? (p.name+' image'): 'Product image'; img.onerror=()=>{ // fallback to no-image state
          if(thumb){ thumb.classList.add('no-image'); }
          img.remove();
        }; } else { // no image
        if(thumb){ thumb.classList.add('no-image'); }
        img.remove(); }
    }
    const title=node.querySelector('[data-ref="title"]'); if(title) title.textContent = p.name||'Unnamed'; const price=node.querySelector('[data-ref="price"]'); if(price) price.textContent = formatCurrency(p.displayPrice); const stock=node.querySelector('[data-ref="stock"]'); if(stock) stock.textContent = (p.stock!=null? p.stock+' in stock':'—'); node.tabIndex=0; node.addEventListener('click', ()=> openEditor(p)); node.addEventListener('keydown', e=>{ if(e.key==='Enter'||e.key===' '){ e.preventDefault(); openEditor(p); }}); return node; }

  function renderPagination(){ if(!els.paginationList) return; els.paginationList.innerHTML=''; const pages = state.totalPages; for(let i=1;i<=pages;i++){ const btn=document.createElement('button'); btn.type='button'; btn.textContent=i; btn.dataset.page=i; btn.className='page-pill'; if(i===state.page) btn.classList.add('active'); els.paginationList.appendChild(btn); } if(els.paginationSummary){ els.paginationSummary.textContent = `${state.totalItems} product${state.totalItems!==1?'s':''} • Page ${state.page} of ${pages}`; } }

  function onPaginationClick(e){ if(e.target.matches('button[data-page]')){ const p=parseInt(e.target.dataset.page,10)||1; if(p!==state.page){ state.page=p; fetchProducts(); } } }

  function toggleSortMenu(){ if(!els.sortMenu||!els.sortToggle) return; const hidden=els.sortMenu.hasAttribute('hidden'); els.sortMenu.toggleAttribute('hidden'); els.sortToggle.setAttribute('aria-expanded', hidden?'true':'false'); }
  function onSortSelect(e){ const opt=e.target.closest('.sort-option'); if(!opt) return; state.sort=opt.dataset.value||'name'; qsa('.sort-option', els.sortMenu).forEach(o=>{ o.classList.remove('active'); o.setAttribute('aria-checked','false'); }); opt.classList.add('active'); opt.setAttribute('aria-checked','true'); toggleSortMenu(); state.page=1; fetchProducts(); }

  function onCategoryTab(e){ const btn=e.target.closest('.tab'); if(!btn) return; qsa('.tab', els.categoryTabs).forEach(t=>{ t.classList.remove('active'); t.setAttribute('aria-selected','false'); }); btn.classList.add('active'); btn.setAttribute('aria-selected','true'); state.category=btn.dataset.filter; state.page=1; fetchProducts(); }

  // Editor
  function openEditor(p){ if(!els.editPanel) return; els.editPanel.classList.add('open'); if(els.editImage) els.editImage.src = (p.imageUrl? ctx()+p.imageUrl: ctx()+'/images/placeholder.svg'); if(els.editForm){ els.editForm.name.value = p.name||''; els.editForm.description.value = p.description||''; els.editForm.price.value = p.displayPrice!=null? p.displayPrice:0; els.editForm.stock.value = p.stock!=null? p.stock:0; ensureCategoryOption(p.categoryKey, p.category); if(els.editCategorySelect && p.categoryKey) els.editCategorySelect.value=p.categoryKey; els.editForm.dataset.id = p.id; } }
  function closeEditor(){ els.editPanel?.classList.remove('open'); }
  function switchEditTab(e){ const tab=e.currentTarget; els.editTabs.forEach(t=>{ t.classList.remove('active'); t.setAttribute('aria-selected','false'); }); tab.classList.add('active'); tab.setAttribute('aria-selected','true'); const section=tab.dataset.section; els.editSections.forEach(sec=>{ sec.hidden = sec.dataset.section !== section; }); }

  function onEditSubmit(e){ e.preventDefault(); const id=els.editForm.dataset.id; if(!id) return; const fd=new FormData(els.editForm); const payload={ name:fd.get('name'), description:fd.get('description'), price:parseFloat(fd.get('price'))||0, stock:parseInt(fd.get('stock'),10)||0 }; const catKey=fd.get('category'); const cat=categoryByKey(catKey); if(cat) payload.categoryId=cat.id; putUpdate(id,payload); }

  function putUpdate(id, body){ const {token,header}=csrf(); fetch(ctx()+'/admin/api/products/'+id,{ method:'PUT', headers:{ 'Content-Type':'application/json', ...(token&&header? {[header]:token}: {}) }, body:JSON.stringify(body) }).then(r=> r.ok? r.json(): r.json().then(j=>Promise.reject(j))).then(updated=>{ // refresh current page
      closeEditor(); fetchProducts(); }).catch(err=>{ console.error('Update failed', err); alert('Update failed: '+ (err.error||'Error')); }); }

  function onDiscard(){ if(!els.editForm) return; const id=els.editForm.dataset.id; if(!id){ els.editForm.reset(); closeEditor(); return; } if(!confirm('Delete this product permanently?')) return; deleteProduct(id); }
  function deleteProduct(id){ const {token,header}=csrf(); fetch(ctx()+'/admin/api/products/'+id,{ method:'DELETE', headers:{ ...(token&&header? {[header]:token}: {}) } }).then(r=> r.ok? r.json(): r.json().then(j=>Promise.reject(j))).then(()=>{ closeEditor(); // refresh list
      state.items = state.items.filter(p=> p.id != id); renderGrid(); fetchProducts(); }).catch(err=>{ console.error('Delete failed', err); alert('Delete failed: '+ (err.error||'Error')); }); }

  // Category
  function buildCategorySelect(){ if(!els.editCategorySelect||!window.__CATEGORIES__) return; window.__CATEGORIES__.forEach(c=> ensureCategoryOption(c.key,c.name)); }
  function ensureCategoryOption(key,label){ if(!els.editCategorySelect) return; if(!els.editCategorySelect.querySelector(`option[value="${key}"]`)){ const opt=document.createElement('option'); opt.value=key; opt.textContent=label||key; els.editCategorySelect.appendChild(opt); } }
  function categoryByKey(key){ if(!window.__CATEGORIES__) return null; return window.__CATEGORIES__.find(c=> c.key===key)||null; }

  // Utils
  function formatCurrency(v){ if(v==null) return '$0.00'; try { return new Intl.NumberFormat(undefined,{style:'currency',currency:'USD'}).format(v); } catch(e){ return '$'+v; } }
  function debounce(fn,ms){ let t; return function(){ clearTimeout(t); t=setTimeout(()=>fn.apply(this,arguments),ms); }; }

  if(document.readyState!=='loading') init(); else document.addEventListener('DOMContentLoaded', init);
})();
