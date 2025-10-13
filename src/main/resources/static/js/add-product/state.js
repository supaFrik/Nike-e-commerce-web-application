// Global State Management

// Product data storage
let colorImageData = {}; // Store images by color: { "Black": [images], "White": [images] }
let colorSizeData = {}; // Store sizes by color: { "Black": ["S", "M"], "White": ["XS", "L"] }
let defaultImageData = {}; // Store default image ID by color: { "Black": "imageId1", "White": "imageId2" }
let colorSizeStockData = {}; // NEW: per color -> { sizeLabel: stock }

let currentImageIndex = 0;
let currentColor = null; // No default color; user will add
let selectedCategory = null; // Store selected category

const maxImages = 10;
let defaultSizes = ['36', '37', '38', '39', '40', '41']; // Default available sizes
let availableColors = [];

window.AppState = {
    getColorImageData: () => colorImageData,
    getColorSizeData: () => colorSizeData,
    getDefaultImageData: () => defaultImageData,
    getColorSizeStockData: () => colorSizeStockData,

    getCurrentImageIndex: () => currentImageIndex,
    getCurrentColor: () => currentColor,
    getSelectedCategory: () => selectedCategory,
    getMaxImages: () => maxImages,
    getDefaultSizes: () => defaultSizes,
    getAvailableColors: () => availableColors,
    
    setCurrentImageIndex: (index) => { currentImageIndex = index; },
    setCurrentColor: (color) => { currentColor = color; },
    setSelectedCategory: (category) => { selectedCategory = category; },
    setAvailableColors: (colors) => { availableColors = colors; },
    
    setColorImageData: (color, images) => { colorImageData[color] = images; },
    setColorSizeData: (color, sizes) => { colorSizeData[color] = sizes; },
    setDefaultImageData: (color, imageId) => { defaultImageData[color] = imageId; },
    setColorSizeStockData: (color, stockMap) => { colorSizeStockData[color] = stockMap; },

    addColor: (color) => {
        if (!availableColors.includes(color)) {
            availableColors.push(color);
            colorImageData[color] = [];
            colorSizeData[color] = ['40'];
            colorSizeStockData[color] = { '40': 0 };
        }
    },
    
    removeColor: (color) => {
        availableColors = availableColors.filter(c => c !== color);
        delete colorImageData[color];
        delete colorSizeData[color];
        delete colorSizeStockData[color]; // remove stock map
        delete defaultImageData[color];
    },
    
    initialize: () => {
        availableColors.forEach(color => {
            if (!colorImageData[color]) { colorImageData[color] = []; }
            if (!colorSizeData[color]) { colorSizeData[color] = ['40']; }
            if (!colorSizeStockData[color]) { colorSizeStockData[color] = { '40': 0 }; }
        });
    },
    
    reset: () => {
        colorImageData = {};
        colorSizeData = {};
        colorSizeStockData = {};
        defaultImageData = {};
        currentImageIndex = 0;
        currentColor = null;
        selectedCategory = null;
        availableColors = [];
        window.AppState.initialize();
    }
};

Object.defineProperty(window.AppState, 'availableColors', {
    get: () => availableColors,
    set: (colors) => { availableColors = colors; }
});

Object.defineProperty(window.AppState, 'colorImageData', {
    get: () => colorImageData,
    set: (data) => { colorImageData = data; }
});

Object.defineProperty(window.AppState, 'colorSizeData', {
    get: () => colorSizeData,
    set: (data) => { colorSizeData = data; }
});

Object.defineProperty(window.AppState, 'defaultImageData', {
    get: () => defaultImageData,
    set: (data) => { defaultImageData = data; }
});

Object.defineProperty(window.AppState, 'colorSizeStockData', {
    get: () => colorSizeStockData,
    set: (data) => { colorSizeStockData = data; }
});

Object.defineProperty(window.AppState, 'currentColor', {
    get: () => currentColor,
    set: (color) => { currentColor = color; }
});

Object.defineProperty(window.AppState, 'currentImageIndex', {
    get: () => currentImageIndex,
    set: (index) => { currentImageIndex = index; }
});

Object.defineProperty(window.AppState, 'selectedCategory', {
    get: () => selectedCategory,
    set: (category) => { selectedCategory = category; }
});

Object.defineProperty(window.AppState, 'maxImages', {
    get: () => maxImages
});