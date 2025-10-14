<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results</title>
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <link rel="stylesheet" href="${env}/css/customer/search/search.css">
    <link rel="stylesheet" href="${env}/css/common/base.css">
</head>
<body class="search-page">
<div class="container search-page-container">
    <!-- Search Header -->
    <div class="search-header">
        <!-- Close (X) Button -->
        <button type="button" class="search-close-btn" id="closeSearchBtn" aria-label="Close search and go back" title="Close">&times;</button>
        <div class="search-info">
            <div class="search-input-wrapper">
                <form action="${env}/search" method="get" id="searchForm">
                    <input type="text" class="search-input" id="liveSearchInput" name="q" placeholder="Search for products..." value="${fn:escapeXml(query)}" autocomplete="off" />
                    <c:if test="${not empty category}">
                        <input type="hidden" name="category" value="${category}" />
                    </c:if>
                    <c:if test="${not empty sort}">
                        <input type="hidden" name="sort" value="${sort}" />
                    </c:if>
                </form>
                <!-- Live search suggestion -->
                <div class="live-search-status" id="liveSearchStatus" aria-live="polite" aria-atomic="true"></div>
            </div>
            <p class="search-results-count" id="resultsCount">
                <c:choose>
                    <c:when test="${searchResult.total == 0}">No results</c:when>
                    <c:otherwise>Showing ${searchResult.total} result<c:if test="${searchResult.total > 1}">s</c:if></c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>

    <!-- Filters Row -->
    <div class="category-filter-row">
        <div class="category-scroll">
            <ul class="category-list">
                <li class="category-item"><a href="${env}/search?q=${fn:escapeXml(query)}" class="category-link ${empty category ? 'active' : ''}">All</a></li>
                <c:forEach var="cat" items="${categories}">
                    <c:set var="catName" value="${cat.name}" />
                    <li class="category-item">
                        <a href="${env}/search?q=${fn:escapeXml(query)}&category=${fn:escapeXml(catName)}"
                           class="category-link ${catName==category ? 'active' : ''}">${catName}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <!-- Replace old select sort with dropdown -->
        <div class="sort-wrapper" style="margin-left:auto;">
            <button type="button" class="sort-btn" id="sortToggleBtn" aria-haspopup="true" aria-expanded="false">
                Sort By
                <span class="sort-chevron" aria-hidden="true"></span>
            </button>
            <div class="sort-dropdown" id="sortMenu" role="menu" aria-label="Sort products">
                <button class="sort-option ${sort=='newest' || empty sort ? 'active' : ''}" data-sort="newest" role="menuitem" type="button">Newest</button>
                <button class="sort-option ${sort=='price-low' ? 'active' : ''}" data-sort="price-low" role="menuitem" type="button">Price: Low-High</button>
                <button class="sort-option ${sort=='price-high' ? 'active' : ''}" data-sort="price-high" role="menuitem" type="button">Price: High-Low</button>
                <button class="sort-option ${sort=='featured' ? 'active' : ''}" data-sort="featured" role="menuitem" type="button">Featured</button>
            </div>
            <form id="sortHiddenForm" action="${env}/search" method="get" style="display:none;">
                <input type="hidden" name="q" value="${fn:escapeXml(query)}" />
                <c:if test="${not empty category}">
                    <input type="hidden" name="category" value="${category}" />
                </c:if>
                <input type="hidden" name="sort" id="sortHiddenInput" value="${sort}" />
            </form>
        </div>
    </div>

    <!-- Product Grid -->
    <div class="product-grid" id="productGrid" style="margin-top:1.5rem;">
        <c:choose>
            <c:when test="${empty products}">
                <div style="padding:2rem; font-weight:600;">No products found matching your search.</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="p" items="${products}">
                    <div class="product-card" style="cursor:pointer;" onclick="location.href='${env}/product-detail?id=${p.id}'">
                        <div class="product-image-wrapper">
                            <c:choose>
                                <c:when test="${not empty p.images and p.images[0] ne null and not empty p.images[0].url}">
                                    <c:set var="imgUrl" value="${p.images[0].url}" />
                                    <c:if test="${fn:startsWith(imgUrl,'//')}">
                                        <c:set var="imgUrl" value="${fn:substring(imgUrl, 1, fn:length(imgUrl))}" />
                                    </c:if>
                                    <c:choose>
                                        <c:when test="${fn:startsWith(imgUrl,'/images/products/')}">
                                            <c:set var="resolvedImg" value="${imgUrl}" />
                                        </c:when>
                                        <c:when test="${fn:startsWith(imgUrl,'images/products/')}">
                                            <c:set var="resolvedImg" value="/${imgUrl}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="resolvedImg" value="/images/products/${imgUrl}" />
                                        </c:otherwise>
                                    </c:choose>
                                    <img src="${env}${resolvedImg}" alt="${p.name}" class="product-image" />
                                </c:when>
                                <c:otherwise>
                                    <img src="${env}/images/products/default-product.avif" alt="${p.name}" class="product-image" />
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="product-info">
                            <c:if test="${p.hasSale}">
                                <p class="product-badge">Sale</p>
                            </c:if>
                            <h3 class="product-name">${p.name}</h3>
                            <p class="product-category">${p.categoryName}</p>
                            <p class="product-price">
                                <c:choose>
                                    <c:when test="${p.hasSale}">
                                        <span style="color:red;font-weight:600;"><fmt:formatNumber value="${p.salePrice}" type="number" maxFractionDigits="0"/>₫</span>
                                        <span style="text-decoration:line-through;color:#666;margin-left:6px"><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>₫</span>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>₫
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Pagination -->
    <c:if test="${searchResult.totalPages > 1}">
        <div class="pagination" id="paginationContainer" style="margin:2rem 0; display:flex; gap:.5rem; flex-wrap:wrap;">
            <c:forEach var="i" begin="1" end="${searchResult.totalPages}">
                <c:url var="pageUrl" value="/search">
                    <c:param name="page" value="${i}" />
                    <c:param name="q" value="${query}" />
                    <c:if test="${not empty category}"><c:param name="category" value="${category}" /></c:if>
                    <c:if test="${not empty sort}"><c:param name="sort" value="${sort}" /></c:if>
                </c:url>
                <a href="${env}${pageUrl}" class="page-link" style="padding:.6rem 1rem; border:1px solid #ccc; border-radius:4px; text-decoration:none; ${i==searchResult.page? 'background:#111;color:#fff;font-weight:600;' : ''}">${i}</a>
            </c:forEach>
        </div>
    </c:if>
</div>

<script>
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
  function escapeHtml(str){ return (str||'').replace(/[&<>"']/g, function(c){ return {"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#39;"}[c]; }); }
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
      priceHtml = '<span style="color:red;font-weight:600;">' + formatPrice(p.salePrice) + '₫</span>'+
                  ' <span style="text-decoration:line-through;color:#666;margin-left:6px">' + formatPrice(p.price) + '₫</span>';
    } else {
      priceHtml = formatPrice(p.price) + '₫';
    }
    var html = '';
    html += '<div class="product-card" style="cursor:pointer;" data-id="'+ p.id +'" onclick="location.href=\''+ baseEnv + '/product-detail?id=' + p.id + '\'">';
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
</script>
</body>
</html>
