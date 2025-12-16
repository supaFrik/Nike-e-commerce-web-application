(function() {
    function activate(item) {
        const mainImage = document.getElementById('currentImage');
        if (!mainImage || !item) return;
        const img = item.querySelector('img');
        if (!img) return;
        mainImage.src = img.src;
        mainImage.alt = img.alt || '';
        document.querySelectorAll('.thumbnail-item').forEach(el => {
            el.classList.toggle('active', el === item);
            el.setAttribute('aria-selected', el === item ? 'true' : 'false');
        });
    }

    function handlePointer(e) {
        const item = e.target.closest('.thumbnail-item');
        if (item && item.parentElement.classList.contains('thumbnail-nav')) activate(item);
    }

    function handleKey(e) {
        if (!['ArrowLeft','ArrowRight','Enter',' '].includes(e.key)) return;
        const container = document.querySelector('.thumbnail-nav');
        const items = Array.from(container.querySelectorAll('.thumbnail-item'));
        if (!items.length) return;
        let current = container.querySelector('.thumbnail-item.active') || items[0];
        let idx = items.indexOf(current);
        if (e.key === 'ArrowRight') {
            idx = (idx + 1) % items.length;
            activate(items[idx]);
            items[idx].focus();
            e.preventDefault();
        } else if (e.key === 'ArrowLeft') {
            idx = (idx - 1 + items.length) % items.length;
            activate(items[idx]);
            items[idx].focus();
            e.preventDefault();
        } else if (e.key === 'Enter' || e.key === ' ') {
            activate(current);
            e.preventDefault();
        }
    }

    function decorateItems() {
        const container = document.querySelector('.thumbnail-nav');
        if (!container) return;
        const items = Array.from(container.querySelectorAll('.thumbnail-item'));
        items.forEach(item => {
            item.setAttribute('tabindex', '0');
            item.setAttribute('role', 'button');
            item.setAttribute('aria-selected', item.classList.contains('active') ? 'true' : 'false');
        });
        if (!container.querySelector('.thumbnail-item.active') && items[0]) {
            items[0].classList.add('active');
            items[0].setAttribute('aria-selected', 'true');
            activate(items[0]);
        }
    }

    function bind() {
        const container = document.querySelector('.thumbnail-nav');
        if (!container) return;
        container.removeEventListener('mouseover', handlePointer);
        container.removeEventListener('click', handlePointer);
        container.removeEventListener('keydown', handleKey);
        container.addEventListener('mouseover', handlePointer);
        container.addEventListener('click', handlePointer);
        container.addEventListener('keydown', handleKey);
    }

    function init() {
        decorateItems();
        bind();
    }

    window.ThumbnailGallery = {
        init: init,
        refresh: init
    };

    document.addEventListener('DOMContentLoaded', init);
})();
