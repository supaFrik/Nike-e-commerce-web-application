function updateThumbnailsForColor(colorName) {
                        var thumbnailContainer = document.querySelector('.thumbnail-nav');
                        if (!thumbnailContainer) return;

                        thumbnailContainer.innerHTML = '';

                        var selectedColor = productColors.find(function(color) {
                            return color.colorName === colorName;
                        });

                        if (!selectedColor) return;

                        var imageCount = 10;
                        for (var i = 1; i <= imageCount; i++) {
                            var thumbnailItem = document.createElement('div');
                            thumbnailItem.className = 'thumbnail-item' + (i === 1 ? ' active' : '');
                            thumbnailItem.setAttribute('data-index', i - 1);
                            thumbnailItem.setAttribute('role', 'listitem');
                            thumbnailItem.setAttribute('tabindex', i);
                            thumbnailItem.onclick = function() { selectThumbnail(this); };

                            var img = document.createElement('img');
                            var imagePath = envPath + '/images/products/' +
                                           encodeURIComponent(productName) + '/' +
                                           encodeURIComponent(selectedColor.folderPath) + '/' +
                                           encodeURIComponent(selectedColor.baseImage.toUpperCase().replace(/\s+/g, '')) +
                                           '-' + i + '.avif';

                            img.src = imagePath;
                            img.alt = 'Product image ' + i;
                            img.onerror = function() {
                            this.onerror = null;
                                this.src = envPath + '/images/products/default-product.avif';
                            };

                            thumbnailItem.appendChild(img);
                            thumbnailContainer.appendChild(thumbnailItem);
                        }

                        var firstThumbnail = thumbnailContainer.querySelector('.thumbnail-item');
                        if (firstThumbnail) {
                            selectThumbnail(firstThumbnail);
                        }
                    }

