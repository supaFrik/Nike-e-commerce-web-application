function updateThumbnailsForColor(colorName) {
    const thumbnailContainer = document.querySelector('.thumbnail-nav');
    if (!thumbnailContainer) return;

    thumbnailContainer.innerHTML = '';

    const selectedColor = productColors.find(function(color) {
        return color.colorName === colorName;
    });
    if (!selectedColor) return;

    const productFolder = encodeURIComponent(productName);
    const colorFolder = encodeURIComponent(selectedColor.folderPath);
    const baseImage = selectedColor.baseImage;

    const imageCount = 10;
    for (let i = 1; i <= imageCount; i++) {
        const thumbnailItem = document.createElement('div');
        thumbnailItem.className = 'thumbnail-item' + (i === 1 ? ' active' : '');
        thumbnailItem.setAttribute('data-index', String(i - 1));
        thumbnailItem.setAttribute('role', 'listitem');
        thumbnailItem.setAttribute('tabindex', String(i));
        thumbnailItem.onclick = function() { selectThumbnail(this); };

        const img = document.createElement('img');
        const imagePath = envPath + '/images/products/' +
            productFolder + '/' +
            colorFolder + '/' +
            encodeURIComponent(baseImage) + '-' + i + '.avif';

        img.src = imagePath;
        img.alt = 'Product image ' + i;
        img.onerror = function() {
            this.onerror = null;
            const parent = this.parentElement;
            if (parent) parent.style.display = 'none';
            if (parent && parent.classList.contains('active')) {
                const next = parent.parentElement && parent.parentElement.querySelector('.thumbnail-item:not([style*="display: none"]) img');
                if (next && next.parentElement) selectThumbnail(next.parentElement);
            }
        };

        thumbnailItem.appendChild(img);
        thumbnailContainer.appendChild(thumbnailItem);
    }

    const firstThumbnail = thumbnailContainer.querySelector('.thumbnail-item');
    if (firstThumbnail) {
        selectThumbnail(firstThumbnail);
    }
}
