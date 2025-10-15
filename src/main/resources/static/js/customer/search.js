(function(){
  const baseEnv = '${env}';
  const toggleBtn = document.getElementById('sortToggleBtn');
  const menu = document.getElementById('sortMenu');
  const hiddenInput = document.getElementById('sortHiddenInput');
  const form = document.getElementById('sortHiddenForm');
  const searchInput = document.getElementById('liveSearchInput');
  const productGrid = document.getElementById('productGrid');
  const resultsCount = document.getElementById('resultsCount');
  const paginationContainer = document.getElementById('paginationContainer');
  const statusEl = document.getElementById('liveSearchStatus');
  var currentPage = parseInt(new URLSearchParams(window.location.search).get('page')||'1',10);
  var currentSort = hiddenInput ? hiddenInput.value : (new URLSearchParams(window.location.search).get('sort')||'');
  var currentCategory = new URLSearchParams(window.location.search).get('category')||'';
  var lastQuery = searchInput ? searchInput.value.trim() : '';
  var abortCtrl = null;
  var debounceTimer = null;

  function formatPrice(n){ if(n==null) return ''; try { return Number(n).toLocaleString('vi-VN'); } catch(e){ return n; } }
  function setStatus(msg){ if(statusEl){ statusEl.textContent = msg; } }
  function clearStatus(){ setStatus(''); }
  function escapeHtml(str){ return (str||'').replace(/[&<>"]|'/g, function(c){ return {"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#39;"}[c]; }); }
  function buildProductCard(p){
    var hasSale = !!p.hasSale && p.salePrice!=null && p.price!=null;
    var imgUrl = null;
    if(p.images && p.images.length>0 && p.images[0] && p.images[0].url){ imgUrl = p.images[0].url; }
    if(imgUrl){
      if(imgUrl.indexOf('//')===0) imgUrl = imgUrl.substring(1);
      if(imgUrl.indexOf('/images/products/')!==0){
        if(imgUrl.indexOf('images/products/')===0) imgUrl = '/' + imgUrl; else imgUrl = '/images/products/' + imgUrl;
      }
      imgUrl = baseEnv + imgUrl;
    } else { imgUrl = baseEnv + '/images/products/default-product.avif'; }
    var priceHtml = '';
    if(hasSale){
      priceHtml = '<span style="color:red;font-weight:600;">' + formatPrice(p.salePrice) + '₫</span>'+ ' <span style="text-decoration:line-through;color:#666;margin-left:6px">' + formatPrice(p.price) + '₫</span>';
    } else {
      priceHtml = formatPrice(p.price) + '₫';
    }
    var html = '';
    html += '<div class="product-card" style="cursor:pointer;" data-id="'+ p.id +'" onclick="location.href=\''+ baseEnv + '/product-detail?id=' + p.id + '\''">';
    html +=   '<div class="product-image-wrapper">';
    html +=     '<img src="'+ imgUrl +'" alt="'+ escapeHtml(p.name||'Product') +'" class="product-image" />';
    html +=   '</div>';
    html +=   '<div class="product-info">';
    if(hasSale){ html += '<p class="product-badge">Sale</p>'; }
    html +=     '<h3 class="product-name">'+ escapeHtml(p.name||'') +'</h3>';
    html +=     '<p class="product-category">'+ escapeHtml(p.categoryName||'') +'</p>';
    html +=     '<p class="product-price">' + priceHtml + '</p>';
    html +=   '</div>';
    html += '</div>';
    return html;
  }
  function buildPagination(totalPages,page){
    if(!paginationContainer) return;
    if(totalPages<=1){ paginationContainer.innerHTML=''; paginationContainer.style.display='none'; return; }
    paginationContainer.style.display='flex';
    var html='';
    for(var i=1;i<=totalPages;i++){
      var active = i===page;
      html += '<a href="#" data-page="'+ i +'" class="page-link" style="padding:.6rem 1rem; border:1px solid #ccc; border-radius:4px; text-decoration:none; '+ (active?'background:#111;color:#fff;font-weight:600;':'') +'">'+ i +'</a>';
    }
    paginationContainer.innerHTML = html;
    var links = paginationContainer.querySelectorAll('a.page-link');
    for(var j=0;j<links.length;j++){
      links[j].addEventListener('click', function(e){ e.preventDefault(); var p = parseInt(this.getAttribute('data-page'),10); if(p!==currentPage){ currentPage=p; performSearch(false); } });
    }
  }
  function updateUrl(q){
    var params = new URLSearchParams(window.location.search);
    if(q) params.set('q', q); else params.delete('q');
    if(currentSort) params.set('sort', currentSort); else params.delete('sort');
    if(currentCategory) params.set('category', currentCategory); else params.delete('category');
    params.set('page', String(currentPage));
    var newUrl = window.location.pathname + '?' + params.toString();
    window.history.replaceState(null,'', newUrl);
  }
  function performSearch(isTyping){
    if(!searchInput) return;
    var q = searchInput.value.trim();
    if(q === lastQuery && !isTyping){ return; }
    lastQuery = q;
    if(abortCtrl){ abortCtrl.abort(); }
    abortCtrl = new AbortController();
    var controller = abortCtrl;
    setStatus('Searching...');
    if(productGrid){ productGrid.classList.add('loading'); }
    var params = new URLSearchParams();
    if(q) params.append('q', q);
    if(currentCategory) params.append('category', currentCategory);
    if(currentSort) params.append('sort', currentSort);
    params.append('page', String(currentPage));
    params.append('pageSize','24');
    fetch(baseEnv + '/api/products/search?' + params.toString(), { signal: controller.signal })
      .then(function(r){ if(!r.ok) throw new Error('HTTP '+r.status); return r.json(); })
      .then(function(data){
        if(controller.signal.aborted) return;
        clearStatus();
        var items = data.items || [];
        if(resultsCount){ resultsCount.textContent = (items.length===0? 'No results' : ('Showing ' + data.total + ' result' + (data.total>1?'s':''))); }
        if(productGrid){
          if(items.length===0){ productGrid.innerHTML = '<div style="padding:2rem;font-weight:600;">No products found matching your search.</div>'; }
          else { productGrid.innerHTML = items.map(buildProductCard).join(''); }
          productGrid.classList.remove('loading');
        }
        buildPagination(data.totalPages || 1, data.page || 1);
        updateUrl(q);
      })
      .catch(function(err){ if(err.name==='AbortError') return; setStatus('Error searching'); if(productGrid) productGrid.classList.remove('loading'); });
  }
  function debouncedSearch(){
    if(debounceTimer) clearTimeout(debounceTimer);
    debounceTimer = setTimeout(function(){ currentPage = 1; performSearch(false); }, 350);
    performSearch(true);
  }
  if(searchInput){
    searchInput.addEventListener('input', debouncedSearch);
    searchInput.addEventListener('keydown', function(e){ if(e.key==='Enter'){ e.preventDefault(); currentPage=1; performSearch(false);} });
  }
  function applySort(val){ currentSort = val; currentPage=1; if(hiddenInput) hiddenInput.value = val; performSearch(false); }
  var closeBtn = document.getElementById('closeSearchBtn');
  if(closeBtn){
    closeBtn.addEventListener('click', function(){
      var currentUrl = window.location.href.split('#')[0];
      var ref = document.referrer;
      if(ref && ref !== '' && ref.indexOf(currentUrl) === -1){ window.history.back(); } else { window.location.href = baseEnv + '/'; }
    });
  }
  if(toggleBtn && menu){
    function closeMenu(){ menu.classList.remove('show'); toggleBtn.setAttribute('aria-expanded','false'); toggleBtn.classList.remove('active'); }
    function openMenu(){ menu.classList.add('show'); toggleBtn.setAttribute('aria-expanded','true'); toggleBtn.classList.add('active'); }
    toggleBtn.addEventListener('click', function(e){ e.stopPropagation(); if(menu.classList.contains('show')) closeMenu(); else openMenu(); });
    document.addEventListener('click', function(e){ if(!menu.contains(e.target) && e.target!==toggleBtn){ closeMenu(); } });
    document.addEventListener('keydown', function(e){ if(e.key==='Escape') closeMenu(); });
    var sortOpts = menu.querySelectorAll('.sort-option');
    for(var i=0;i<sortOpts.length;i++){ (function(btn){ btn.addEventListener('click', function(){ var val = btn.getAttribute('data-sort'); applySort(val); closeMenu(); }); })(sortOpts[i]); }
  }
})();

