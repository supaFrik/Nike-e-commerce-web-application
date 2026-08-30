(function(){
  var grid = document.querySelector(".product-grid__items");
  var stateEl = document.getElementById("infinite-scroll-state");
  var sentinel = document.getElementById("infinite-scroll-sentinel");
  var loadingEl = document.getElementById("infinite-scroll-loading");
  var endEl = document.getElementById("infinite-scroll-end");
  if(!grid || !stateEl || !sentinel) return;

  var currentPage = parseInt(stateEl.dataset.currentPage || "0", 10);
  var totalPages = parseInt(stateEl.dataset.totalPages || "0", 10);
  var hasNext = stateEl.dataset.hasNext === "true";
  var categoryId = stateEl.dataset.categoryId || "";
  var sort = stateEl.dataset.sort || "newest";
  var ctx = (window.APP_CTX || "");
  var loading = false;

  function formatVND(v){
    if(v==null) return "0?";
    return Number(v).toLocaleString("vi-VN") + "?";
  }
  var statusMap = {ACTIVE:"Just in",DRAFT:"Coming soon",FEW_LEFT:"Few left",OUT_OF_STOCK:"Sold out",DISCONTINUED:"No longer available"};

  function escapeHtml(s){
    return String(s).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;");
  }

  function createCard(p, idx){
    var priceHtml = "";
    if(p.hasSale && p.salePrice){
      priceHtml = "<span class=\"sale-price\">"+escapeHtml(formatVND(p.salePrice))+"</span><span class=\"orig-price\" style=\"text-decoration:line-through;color:#777;margin-left:4px;\">"+escapeHtml(formatVND(p.price))+"</span>";
    } else {
      priceHtml = escapeHtml(formatVND(p.price));
    }
    var img = p.heroImg ? escapeHtml(p.heroImg) : (ctx + "/images/products/product-1.jpg");
    var status = statusMap[p.status] || p.status || "";
    var cat = escapeHtml(p.categoryName || "");
    var name = escapeHtml(p.name || "");
    return "<div class=\"product-card product-grid__card\" data-product-card=\"true\" role=\"article\"><div class=\"product-card__body\"><figure><a aria-label=\""+name+"\" href=\""+ctx+"/product-detail?id="+p.id+"\" class=\"product-card__img-link-overlay\"><div class=\"wall-image-loader content-card__image\"><img src=\""+img+"\" alt=\""+name+"\" loading=\"lazy\"></div></a><div class=\"product-card__info\"><div class=\"product_msg_info\"><div class=\"product-card__messaging\">"+escapeHtml(status)+"</div><div class=\"product-card__titles\"><div class=\"product-card__title\">"+name+"</div><div class=\"product-card__subtitle\">"+cat+"</div></div></div></div><div class=\"product-card__count-wrapper\"><div class=\"product-card__count-item\"><button class=\"product-card__colorway-btn\" type=\"button\"><div class=\"product-card__product-count\">"+p.colorCount+" Colour"+(p.colorCount>1?"s":"")+"</div></button></div></div><div class=\"product-card__price-wrapper\"><div class=\"product-card__price-container\"><div class=\"product-card__price\">"+priceHtml+"</div></div></div></figure></div></div>";
  }

  function showEnd(){
    if(endEl) endEl.hidden = false;
    observer.disconnect();
  }

  function loadNext(){
    if(loading || !hasNext) return;
    loading = true;
    if(loadingEl) loadingEl.hidden = false;
    var nextPage = currentPage + 1;
    var url = ctx + "/products/list/data?page=" + nextPage + "&sort=" + encodeURIComponent(sort);
    if(categoryId) url += "&categoryId=" + encodeURIComponent(categoryId);
    fetch(url, {headers: {"Accept":"application/json"}})
      .then(function(r){ if(!r.ok) throw new Error("http "+r.status); return r.json(); })
      .then(function(data){
        var content = data.content || [];
        content.forEach(function(p, i){ grid.insertAdjacentHTML("beforeend", createCard(p, i)); });
        currentPage = data.page;
        hasNext = data.hasNext;
        totalPages = data.totalPages;
        stateEl.dataset.currentPage = currentPage;
        stateEl.dataset.hasNext = hasNext;
        if(!hasNext) showEnd();
      })
      .catch(function(e){ console.error(e); })
      .finally(function(){ loading = false; if(loadingEl) loadingEl.hidden = true; });
  }

  if(!hasNext){ showEnd(); return; }

  var observer = new IntersectionObserver(function(entries){
    entries.forEach(function(e){ if(e.isIntersecting) loadNext(); });
  }, {rootMargin: "400px"});

  observer.observe(sentinel);
})();
