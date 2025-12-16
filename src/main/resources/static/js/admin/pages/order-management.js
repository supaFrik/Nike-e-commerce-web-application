// Order Management page script
(function() {
  const env = (typeof window.env !== 'undefined') ? window.env : '';
  function getCsrf() {
    const meta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    return {
      token: meta ? meta.getAttribute('content') : null,
      header: headerMeta ? headerMeta.getAttribute('content') : 'X-CSRF-TOKEN'
    };
  }

  function initSearch() {
    const form = document.querySelector('form[action$="/admin/orders"]');
    if (!form) return;
    const input = form.querySelector('input[name="q"]');
    if (!input) return;
    let debounce;
    input.addEventListener('input', function() {
      clearTimeout(debounce);
      debounce = setTimeout(() => {
        form.submit();
      }, 400);
    });
  }

  function initDelete() {
    const csrf = getCsrf();
    document.querySelectorAll('.js-delete-order').forEach(btn => {
      btn.addEventListener('click', function() {
        const id = this.getAttribute('data-id');
        if (!id) return;
        if (!confirm('Bạn có chắc muốn xóa đơn hàng #' + id + ' ?')) return;
        // Try AJAX delete, fallback handled by inline script
        fetch(`${env}/admin/api/orders/${id}`, {
          method: 'DELETE',
          headers: Object.assign({ 'Content-Type': 'application/json' }, csrf.token ? { [csrf.header]: csrf.token } : {})
        }).then(res => {
          if (res.ok) {
            window.location.reload();
          } else {
            // fallback navigation if API not available
            window.location.href = `${env}/admin/orders/delete?id=${id}`;
          }
        }).catch(() => {
          window.location.href = `${env}/admin/orders/delete?id=${id}`;
        });
      });
    });
  }

  function initTabs() {
    const statusParam = new URL(window.location.href).searchParams.get('status') || 'all';
    document.querySelectorAll('.tab-button').forEach(btn => {
      const s = btn.getAttribute('data-status');
      if (s === statusParam) {
        btn.classList.add('active');
      } else {
        btn.classList.remove('active');
      }
    });
  }

  function injectSampleDataIfEmpty() {
    const tbody = document.querySelector('table tbody');
    if (!tbody) return;
    const hasDataRows = tbody.querySelectorAll('tr').length > 0 && !tbody.querySelector('td[colspan]');
    if (hasDataRows) return;
    const banner = document.createElement('div');
    banner.className = 'my-3 text-xs text-gray-600 bg-yellow-50 border border-yellow-200 rounded px-3 py-2';
    banner.innerHTML = '<span class="font-medium">Dữ liệu mẫu:</span> Các hàng dưới đây chỉ dùng để minh họa giao diện và không liên quan đến hệ thống.';
    const cardBody = document.querySelector('.card .card-body');
    if (cardBody) {
      cardBody.insertBefore(banner, cardBody.querySelector('.overflow-x-auto'));
    }
    // Sample
    const samples = [
      { id: 1001, date: '01 Dec, 2025', customer: 'Demo User', status: 'REVIEW', total: '₫ 250,000', shipping: 'STANDARD', items: 2, fulfilled: false },
      { id: 1002, date: '02 Dec, 2025', customer: 'Jane Doe', status: 'FULFILLED', total: '₫ 480,000', shipping: 'EXPRESS', items: 3, fulfilled: true },
      { id: 1003, date: '03 Dec, 2025', customer: 'John Smith', status: 'PROCESSING', total: '₫ 120,000', shipping: 'STANDARD', items: 1, fulfilled: false }
    ];
    const fragment = document.createDocumentFragment();
    samples.forEach(s => {
      const tr = document.createElement('tr');
      tr.className = 'border-b border-gray-100 hover:bg-gray-50';
      tr.innerHTML = `
        <td class="py-4 px-4"><input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng"></td>
        <td class="py-4 px-4"><a href="#" class="text-sm font-medium text-gray-900 no-underline hover:underline">#${s.id}</a></td>
        <td class="py-4 px-4"><span class="text-sm text-gray-600">${s.date}</span></td>
        <td class="py-4 px-4"><span class="text-sm text-gray-900">${s.customer}</span></td>
        <td class="py-4 px-4">
          <span class="badge ${s.status === 'FULFILLED' ? 'badge--completed' : 'badge--delivery'}">
            ${s.status === 'FULFILLED' ? 'Thành công' : (s.status === 'REVIEW' ? 'Đang xem xét' : 'Đang xử lý')}
          </span>
        </td>
        <td class="py-4 px-4"><span class="text-sm font-medium text-gray-900">${s.total}</span></td>
        <td class="py-4 px-4"><span class="text-sm text-gray-600">${s.shipping}</span></td>
        <td class="py-4 px-4"><span class="text-sm text-gray-600">${s.items} Items</span></td>
        <td class="py-4 px-4">
          <span class="badge ${s.fulfilled ? 'badge--completed' : 'badge--delivery'}">
            ${s.fulfilled ? 'Đã giao' : 'Chưa giao'}
          </span>
        </td>
        <td class="py-4 px-4">
          <div class="flex items-center gap-2">
            <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
              <i class="ti ti-copy text-gray-600"></i>
            </button>
            <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
              <i class="ti ti-trash text-gray-600"></i>
            </button>
          </div>
        </td>`;
      fragment.appendChild(tr);
    });
    // Replace empty state row with samples
    tbody.innerHTML = '';
    tbody.appendChild(fragment);
  }

  document.addEventListener('DOMContentLoaded', function() {
    initSearch();
    initDelete();
    initTabs();
    injectSampleDataIfEmpty();
  });
})();
