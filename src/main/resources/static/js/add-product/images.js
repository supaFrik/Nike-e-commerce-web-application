// Image Management Module
// Handles image upload, display, and management functionality

// Set current color images
function setCurrentColorImages(images) {
    window.AppState.colorImageData[window.AppState.currentColor] = images;
}

// Set default image for current color
function setDefaultImage(imageId) {
    window.AppState.defaultImageData[window.AppState.currentColor] = imageId;
    updateImageDisplay();
}

// Get default image ID for current color
function getDefaultImageId() {
    return window.AppState.defaultImageData[window.AppState.currentColor] || null;
}

// Check if image is default for current color
function isDefaultImage(imageId) {
    return window.AppState.defaultImageData[window.AppState.currentColor] === imageId;
}

// Update image display
function updateImageDisplay() {
    const currentImages = getCurrentColorImages();
    const mainImage = document.getElementById('mainImage');
    const uploadPlaceholder = document.getElementById('uploadPlaceholder');
    const thumbnailsContainer = document.getElementById('imageThumbnails');
    const imageCounter = document.getElementById('imageCounter');
    const addMoreImages = document.getElementById('addMoreImages');
    const addImagesBtn = document.querySelector('.add-images-btn');

    // Update counter (with null check)
    if (imageCounter) {
        imageCounter.textContent = `${currentImages.length} / ${window.AppState.maxImages} images uploaded for ${window.AppState.currentColor}`;
    }

    // Show/hide "Add More Images" button (with null checks)
    if (addMoreImages) {
        if (currentImages.length === 0) {
            addMoreImages.style.display = 'none';
        } else {
            addMoreImages.style.display = 'block';
            
            // Enable/disable button based on image limit
            if (addImagesBtn) {
                if (currentImages.length >= window.AppState.maxImages) {
                    addImagesBtn.disabled = true;
                    addImagesBtn.innerHTML = `
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M3 8h10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                        Maximum Images Reached for ${window.AppState.currentColor}
                    `;
                } else {
                    addImagesBtn.disabled = false;
                    addImagesBtn.innerHTML = `
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                        Add More Images for ${window.AppState.currentColor} (${window.AppState.maxImages - currentImages.length} remaining)
                    `;
                }
            }
        }
    }

    if (currentImages.length === 0) {
        // Show placeholder (with null checks)
        if (mainImage) mainImage.style.display = 'none';
        if (uploadPlaceholder) uploadPlaceholder.style.display = 'block';
        if (thumbnailsContainer) thumbnailsContainer.innerHTML = '';
        return;
    }

    // Ensure currentImageIndex is within bounds
    if (window.AppState.currentImageIndex >= currentImages.length) {
        window.AppState.setCurrentImageIndex(0);
    }

    // Show main image (with null checks)
    if (mainImage) {
        mainImage.style.display = 'block';
        mainImage.src = currentImages[window.AppState.currentImageIndex].src;
    }
    if (uploadPlaceholder) {
        uploadPlaceholder.style.display = 'none';
    }

    // Generate thumbnails (with null check)
    if (thumbnailsContainer) {
        thumbnailsContainer.innerHTML = '';
        
        currentImages.forEach((image, index) => {
            const thumbnailContainer = document.createElement('div');
            thumbnailContainer.className = 'thumbnail-container';

            const thumbnail = document.createElement('img');
            thumbnail.src = image.src;
            thumbnail.alt = `Thumbnail ${index + 1} - ${window.AppState.currentColor}`;
            thumbnail.className = `thumbnail ${index === window.AppState.currentImageIndex ? 'active' : ''}`;
            thumbnail.onclick = () => selectImage(index);

            const removeBtn = document.createElement('button');
            removeBtn.className = 'thumbnail-remove';
            removeBtn.innerHTML = '×';
            removeBtn.title = `Remove image from ${window.AppState.currentColor}`;
            removeBtn.onclick = (e) => {
                e.stopPropagation();
                removeImage(index);
            };

            const defaultBtn = document.createElement('button');
            const isDefault = isDefaultImage(image.id);
            defaultBtn.className = `thumbnail-default ${isDefault ? 'is-default' : ''}`;
            defaultBtn.innerHTML = isDefault ? '★ Default' : 'Set Default';
            defaultBtn.title = isDefault ? 'This is the default image' : 'Set as default image';
            defaultBtn.onclick = (e) => {
                e.stopPropagation();
                setDefaultImage(image.id);
            };

            thumbnailContainer.appendChild(thumbnail);
            thumbnailContainer.appendChild(removeBtn);
            thumbnailContainer.appendChild(defaultBtn);
            thumbnailsContainer.appendChild(thumbnailContainer);
        });
    }
}

// Get current color images
function getCurrentColorImages() {
    return window.AppState.colorImageData[window.AppState.currentColor] || [];
}

// Select image for main display
function selectImage(index) {
    window.AppState.setCurrentImageIndex(index);
    updateImageDisplay();
}

// Remove image
function removeImage(index) {
    const currentImages = getCurrentColorImages();
    const removedImage = currentImages[index];
    
    // Check if removing default image
    const wasDefault = isDefaultImage(removedImage.id);
    
    currentImages.splice(index, 1);
    setCurrentColorImages(currentImages);
    
    // If removed image was default, set new default
    if (wasDefault && currentImages.length > 0) {
        setDefaultImage(currentImages[0].id);
    } else if (wasDefault) {
        // No images left, clear default
        delete window.AppState.defaultImageData[window.AppState.currentColor];
    }
    
    // Adjust current image index if necessary
    if (window.AppState.currentImageIndex >= currentImages.length) {
        window.AppState.setCurrentImageIndex(Math.max(0, currentImages.length - 1));
    }
    
    updateImageDisplay();
}

// Process uploaded files
function processUploadedFiles(files) {
    const currentImages = getCurrentColorImages();
    
    // Filter only image files
    const imageFiles = files.filter(file => file.type.startsWith('image/'));
    
    // Check if adding these files would exceed the limit
    if (currentImages.length + imageFiles.length > window.AppState.maxImages) {
        window.Toast.show(`You can only upload a maximum of ${window.AppState.maxImages} images for ${window.AppState.currentColor}. Currently you have ${currentImages.length} images.`);
        return;
    }

    // Process multiple files and collect results
    let processedCount = 0;
    const newImages = [];

    imageFiles.forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = function(e) {
            const imageData = {
                src: e.target.result,
                name: file.name,
                id: Date.now() + Math.random() + index, // Add index to ensure unique IDs
                color: window.AppState.currentColor
            };
            newImages.push(imageData);
            processedCount++;
            
            // When all files are processed, update the images array once
            if (processedCount === imageFiles.length) {
                const updatedImages = [...currentImages, ...newImages];
                setCurrentColorImages(updatedImages);
                
                // Set first image as default if no default exists for this color
                if (!getDefaultImageId() && updatedImages.length > 0) {
                    setDefaultImage(updatedImages[0].id);
                } else {
                    updateImageDisplay();
                }
            }
        };
        reader.readAsDataURL(file);
    });
}

// Prevent default drag behaviors
function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
}

// Image upload functionality and drag and drop
function initializeImageHandlers() {
    const uploadArea = document.getElementById('uploadArea');
    const imageUpload = document.getElementById('imageUpload');

    if (!uploadArea || !imageUpload) return;

    // Image upload change event
    imageUpload.addEventListener('change', function(e) {
        const files = Array.from(e.target.files);
        processUploadedFiles(files);
        
        // Clear the input so the same files can be selected again if needed
        e.target.value = '';
    });

    // Drag and drop functionality
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        uploadArea.addEventListener(eventName, preventDefaults, false);
    });

    ['dragenter', 'dragover'].forEach(eventName => {
        uploadArea.addEventListener(eventName, () => {
            uploadArea.classList.add('drag-over');
        });
    });

    ['dragleave', 'drop'].forEach(eventName => {
        uploadArea.addEventListener(eventName, () => {
            uploadArea.classList.remove('drag-over');
        });
    });

    uploadArea.addEventListener('drop', (e) => {
        const files = Array.from(e.dataTransfer.files);
        processUploadedFiles(files);
    });
}

// Export for global access
window.ImageManager = {
    updateDisplay: updateImageDisplay,
    getCurrentImages: getCurrentColorImages,
    setCurrentImages: setCurrentColorImages,
    setDefaultImage: setDefaultImage,
    getDefaultImageId: getDefaultImageId,
    isDefaultImage: isDefaultImage,
    selectImage: selectImage,
    removeImage: removeImage,
    processFiles: processUploadedFiles,
    initializeHandlers: initializeImageHandlers
};