/* Admin Products Interaction Script */
const state = {
  products: [],
  filtered: [],
  page: 1,
  pageSize: 10,
  current: null,
  filter: 'all',
  sort: 'name',
  query: ''
};

function createSampleProducts() {
  const categories = ['running','basketball','lifestyle'];
  const imgs = [
    'https://via.placeholder.com/300x220?text=Shoes+1',
    'https://via.placeholder.com/300x220?text=Shoes+2',
    'https://via.placeholder.com/300x220?text=Shoes+3',
    'https://via.placeholder.com/300x220?text=Shoes+4'
  ];
  const list = [];
  for (let i=1;i<=48;i++) {
    list.push({
      id: i,
      name: `Sample Shoe ${i}`,
      description: 'Minimal breathable comfort and everyday versatility.',
      category: categories[i % categories.length],
      price: (50 + i).toFixed(2),
      stock: 100 - i,
      image: imgs[i % imgs.length]
    });
  }
  return list;
}

function applyFilters() {
  const lower = state.query.toLowerCase();
  state.filtered = state.products
    .filter(p => {
      let categoryMatch = true;
      if(state.filter === 'most') {
        // Simulate most purchased: arbitrary rule top 15 by stock descending (pretend purchased) => lower stock means more sold
        return true; // filter after collecting list
      } else if(state.filter !== 'all') {
        categoryMatch = p.category === state.filter;
      }
      const textMatch = !lower || p.name.toLowerCase().includes(lower);
      return categoryMatch && textMatch;
    });
  // If 'most' filter selected, slice after sorting by (100 - stock) descending to simulate sales volume
  if(state.filter === 'most') {
    state.filtered = [...state.filtered].sort((a,b)=> ( (100 - b.stock) - (100 - a.stock) ) ).slice(0,15);
  }
  state.filtered.sort((a,b)=>{
    switch(state.sort){
      case 'price': return parseFloat(a.price) - parseFloat(b.price);
      case 'stock': return a.stock - b.stock;
      default: return a.name.localeCompare(b.name);
    }
  });
  state.page = 1;
  renderGrid();
}

function paginate(items){
  const start = (state.page - 1) * state.pageSize;
  return items.slice(start, start + state.pageSize);
}

let lastRenderReason = 'init'; // track why grid was rendered
function renderGrid(){
  const grid = document.getElementById('productGrid');
  grid.setAttribute('aria-busy','true');
  grid.innerHTML='';
  const pageItems = paginate(state.filtered);
  const tmpl = document.getElementById('productCardTemplate');
  pageItems.forEach((p,idx)=>{
    const node = tmpl.content.firstElementChild.cloneNode(true);
    node.dataset.id = p.id;
    node.querySelector('[data-ref=image]').src = p.image;
    node.querySelector('[data-ref=image]').alt = p.name;
    node.querySelector('[data-ref=title]').textContent = p.name;
    node.querySelector('[data-ref=price]').textContent = `$${p.price}`;
    node.querySelector('[data-ref=stock]').textContent = `Stock ${p.stock}`;
    if(state.current && state.current.id === p.id){
      node.classList.add('selected');
      node.setAttribute('aria-pressed','true');
    }
    // Add enter animation unless we are re-rendering due to selection
    if(lastRenderReason !== 'selection'){
      node.classList.add('anim-enter');
      node.style.transitionDelay = (idx * 40)+'ms'; // stagger
    }
    node.addEventListener('click',()=>selectProduct(p.id));
    node.addEventListener('keydown',e=>{ if(e.key==='Enter' || e.key===' '){ e.preventDefault(); selectProduct(p.id);} });
    grid.appendChild(node);
  });
  renderPagination();
  grid.removeAttribute('aria-busy');
  // Scroll to top when changing page/filter/search (not selection)
  if(lastRenderReason !== 'selection'){
    grid.scrollTo({top:0,behavior:'smooth'});
  }
  // Trigger animation after next frame
  if(lastRenderReason !== 'selection'){
    requestAnimationFrame(()=>{
      grid.querySelectorAll('.anim-enter').forEach(el=>{
        requestAnimationFrame(()=> el.classList.add('anim-in'));
      });
    });
  } else {
    // Clean any stale animation classes
    grid.querySelectorAll('.anim-enter').forEach(el=> el.classList.remove('anim-enter','anim-in'));
  }
  lastRenderReason = 'normal';
}

function selectProduct(id){
  const product = state.products.find(p=>p.id==id);
  if(!product) return;
  state.current = product;
  populateForm(product);
  lastRenderReason = 'selection';
  renderGrid();
  openPanel();
}

function populateForm(p){
  const form = document.getElementById('editForm');
  form.name.value = p.name;
  form.description.value = p.description;
  form.category.value = p.category;
  form.price.value = p.price;
  form.stock.value = p.stock;
  document.getElementById('editImage').src = p.image;
  document.getElementById('editImage').alt = p.name;
}

function openPanel(){
  document.querySelector('.edit-panel').classList.add('open');
}
function closePanel(){
  document.querySelector('.edit-panel').classList.remove('open');
}

function wireEvents(){
  document.getElementById('search').addEventListener('input', e=>{ state.query = e.target.value; applyFilters(); });
  // Custom sort menu interactions
  const sortToggle = document.getElementById('sortToggle');
  const sortMenu = document.getElementById('sortMenu');
  const hiddenSelect = document.getElementById('sort');
  const sortLabelSpan = sortToggle?.querySelector('.sort-label');
  const sortWrapper = sortToggle?.closest('.sort-wrapper');
  if(!sortToggle || !sortMenu){
    console.warn('Sort components not found');
  }
  function closeSort(){
    if(!sortMenu || !sortToggle) return;
    sortMenu.hidden = true;
    sortToggle.setAttribute('aria-expanded','false');
    sortWrapper && sortWrapper.classList.remove('open');
  }
  function openSort(){
    if(!sortMenu || !sortToggle) return;
    sortMenu.hidden = false;
    sortToggle.setAttribute('aria-expanded','true');
    sortWrapper && sortWrapper.classList.add('open');
    // focus first option
    const first = sortMenu.querySelector('.sort-option');
    if(first) first.focus();
  }
  sortToggle?.addEventListener('click',(e)=>{
    e.stopPropagation();
    const expanded = sortToggle.getAttribute('aria-expanded') === 'true';
    expanded ? closeSort() : openSort();
  });
  sortMenu?.addEventListener('click', e=>{
    const btn = e.target.closest('.sort-option');
    if(!btn) return;
    const value = btn.dataset.value;
    state.sort = value;
    hiddenSelect.value = value; // sync fallback select
    if(sortLabelSpan){ sortLabelSpan.textContent = 'Sort by'; }
    sortMenu.querySelectorAll('.sort-option').forEach(o=>{ o.classList.remove('active'); o.setAttribute('aria-checked','false'); });
    btn.classList.add('active');
    btn.setAttribute('aria-checked','true');
    closeSort();
    applyFilters();
    sortToggle.focus();
  });
  document.addEventListener('click', e=>{
    if(sortMenu && !sortMenu.hidden && !e.target.closest('[data-sort]')) closeSort();
  });
  document.addEventListener('keydown', e=>{
    if(e.key === 'Escape' && !sortMenu.hidden){ closeSort(); sortToggle.focus(); }
    if(!sortMenu.hidden && (e.key === 'ArrowDown' || e.key === 'ArrowUp')){
      e.preventDefault();
      const options = Array.from(sortMenu.querySelectorAll('.sort-option'));
      const currentIndex = options.indexOf(document.activeElement);
      let nextIndex = currentIndex;
      if(e.key === 'ArrowDown') nextIndex = (currentIndex + 1) % options.length;
      else nextIndex = (currentIndex - 1 + options.length) % options.length;
      options[nextIndex].focus();
    }
    if(e.key === ' ' && document.activeElement === sortToggle){
      e.preventDefault();
      const expanded = sortToggle.getAttribute('aria-expanded') === 'true';
      expanded ? closeSort() : openSort();
    }
    if(!sortMenu.hidden && e.key === 'Enter'){
      const focused = document.activeElement;
      if(focused.classList.contains('sort-option')) focused.click();
    }
  });
  // Close sort when tabbing away (focusout) - delay to allow next focused element detection
  sortMenu?.addEventListener('focusout', (e)=>{
    setTimeout(()=>{
      if(!sortMenu.contains(document.activeElement) && document.activeElement !== sortToggle){
        closeSort();
      }
    },0);
  });
  document.querySelectorAll('.tab').forEach(btn=> btn.addEventListener('click',()=>{
    document.querySelectorAll('.tab').forEach(b=>{b.classList.remove('active');b.setAttribute('aria-selected','false');});
    btn.classList.add('active');btn.setAttribute('aria-selected','true');
    state.filter = btn.dataset.filter; applyFilters();
  }));
  // (Removed obsolete prev button listener; handled in renderPagination())
  // (Legacy next button removed with new pagination UI)
  document.getElementById('closeEdit').addEventListener('click', closePanel);
  document.getElementById('editForm').addEventListener('submit', e=>{
    e.preventDefault();
    if(!state.current) return;
    const form = e.target;
    state.current.name = form.name.value.trim();
    state.current.description = form.description.value.trim();
    state.current.category = form.category.value;
    state.current.price = parseFloat(form.price.value).toFixed(2);
    state.current.stock = parseInt(form.stock.value,10) || 0;
    populateForm(state.current);
    renderGrid();
    form.querySelector('[type=submit]').textContent = 'Saved!';
    setTimeout(()=> form.querySelector('[type=submit]').textContent = 'Update Product', 1200);
  });
  document.getElementById('addProductBtn').addEventListener('click', ()=>{
    const id = state.products.length + 1;
    const newItem = { id, name:`New Shoe ${id}`, description:'Describe the product...', category:'running', price:'0.00', stock:0, image:'https://via.placeholder.com/300x220?text=New+Shoe'};
    state.products.unshift(newItem);
    applyFilters();
    selectProduct(id);
  });
  window.addEventListener('keydown', e=>{ if(e.key==='Escape'){ closePanel(); }});

  // Edit panel tabs
  const editTabs = document.querySelectorAll('.edit-tab');
  const sections = document.querySelectorAll('.edit-section');
  editTabs.forEach(tab=>{
    tab.addEventListener('click',()=>{
      const target = tab.dataset.section;
      editTabs.forEach(t=>{t.classList.remove('active'); t.setAttribute('aria-selected','false');});
      sections.forEach(sec=>{
        if(sec.dataset.section === target){ sec.hidden = false; } else { sec.hidden = true; }
      });
      tab.classList.add('active');
      tab.setAttribute('aria-selected','true');
    });
  });
}

// Build pagination pages with ellipsis strategy
function computePages(totalPages, current){
  const pages = [];
  if(totalPages <= 7){
    for(let i=1;i<=totalPages;i++) pages.push(i);
    return pages;
  }
  const first = 1, last = totalPages;
  const showLeft = current <= 4;
  const showRight = current >= totalPages - 3;
  if(showLeft){
    pages.push(1,2,3,4,5,'...',last);
  } else if(showRight){
    pages.push(first,'...', last-4,last-3,last-2,last-1,last);
  } else {
    pages.push(first,'...', current-1,current,current+1,'...', last);
  }
  return pages;
}

function renderPagination(){
  const list = document.getElementById('paginationList');
  if(!list) return;
  const totalPages = Math.ceil(state.filtered.length / state.pageSize) || 1;
  if(state.page > totalPages) state.page = totalPages;
  list.innerHTML='';
  // Summary text
  const summaryEl = document.getElementById('paginationSummary');
  const total = state.filtered.length;
  const start = total === 0 ? 0 : ( (state.page - 1) * state.pageSize ) + 1;
  const end = Math.min(state.page * state.pageSize, total);
  if(summaryEl){
    summaryEl.textContent = `Showing ${start} to ${end} of ${total} results`;
  }
  // Prev button
  const prev = document.createElement('li');
  const prevBtn = document.createElement('button');
  prevBtn.className = 'page-pill';
  prevBtn.textContent = '‹';
  prevBtn.setAttribute('aria-label','Previous page');
  if(state.page === 1) prevBtn.disabled = true; else prevBtn.addEventListener('click',()=>{ state.page--; renderGrid(); });
  prev.appendChild(prevBtn); list.appendChild(prev);
  // Numbered pages
  computePages(totalPages,state.page).forEach(p=>{
    const li = document.createElement('li');
    if(p === '...'){
      const span = document.createElement('span');
      span.className = 'page-ellipsis';
      span.textContent = '…';
      span.setAttribute('aria-hidden','true');
      li.appendChild(span);
    } else {
      const btn = document.createElement('button');
      btn.className = 'page-pill';
      btn.textContent = p;
      btn.setAttribute('aria-label',`Page ${p}`);
      if(p === state.page){
        btn.classList.add('active');
        btn.setAttribute('aria-current','page');
      } else {
        btn.addEventListener('click',()=>{ state.page = p; renderGrid(); });
      }
      li.appendChild(btn);
    }
    list.appendChild(li);
  });
  // Next button
  const next = document.createElement('li');
  const nextBtn = document.createElement('button');
  nextBtn.className = 'page-pill';
  nextBtn.textContent = '›';
  nextBtn.setAttribute('aria-label','Next page');
  if(state.page >= totalPages) nextBtn.disabled = true; else nextBtn.addEventListener('click',()=>{ state.page++; renderGrid(); });
  next.appendChild(nextBtn); list.appendChild(next);
}

function init(){
  state.products = createSampleProducts();
  applyFilters();
  wireEvents();
}

document.addEventListener('DOMContentLoaded', init);
