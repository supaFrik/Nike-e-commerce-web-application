// Global State Management
// This module manages all the global variables and state for the product form

// Product data storage
let colorImageData = {}; // Store images by color: { "Black": [images], "White": [images] }
let colorSizeData = {}; // Store sizes by color: { "Black": ["S", "M"], "White": ["XS", "L"] }
let defaultImageData = {}; // Store default image ID by color: { "Black": "imageId1", "White": "imageId2" }
let colorSizeStockData = {}; // NEW: per color -> { sizeLabel: stock }

// UI state variables
let currentImageIndex = 0;
let currentColor = 'Black'; // Currently selected color
let selectedCategory = null; // Store selected category

// Configuration constants
const maxImages = 10;
let defaultSizes = ['36', '37', '38', '39', '40', '41']; // Default available sizes
let availableColors = ['Black']; // Default colors

// State getters and setters
window.AppState = {
    // Data getters
    getColorImageData: () => colorImageData,
    getColorSizeData: () => colorSizeData,
    getDefaultImageData: () => defaultImageData,
    getColorSizeStockData: () => colorSizeStockData,

    // UI state getters
    getCurrentImageIndex: () => currentImageIndex,
    getCurrentColor: () => currentColor,
    getSelectedCategory: () => selectedCategory,
    getMaxImages: () => maxImages,
    getDefaultSizes: () => defaultSizes,
    getAvailableColors: () => availableColors,
    
    // State setters
    setCurrentImageIndex: (index) => { currentImageIndex = index; },
    setCurrentColor: (color) => { currentColor = color; },
    setSelectedCategory: (category) => { selectedCategory = category; },
    setAvailableColors: (colors) => { availableColors = colors; },
    
    // Data manipulation methods
    setColorImageData: (color, images) => { colorImageData[color] = images; },
    setColorSizeData: (color, sizes) => { colorSizeData[color] = sizes; },
    setDefaultImageData: (color, imageId) => { defaultImageData[color] = imageId; },
    setColorSizeStockData: (color, stockMap) => { colorSizeStockData[color] = stockMap; },

    // Utility methods
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
    
    // Initialize default data
    initialize: () => {
        availableColors.forEach(color => {
            if (!colorImageData[color]) { colorImageData[color] = []; }
            if (!colorSizeData[color]) { colorSizeData[color] = ['40']; }
            if (!colorSizeStockData[color]) { colorSizeStockData[color] = { '40': 0 }; }
        });
    },
    
    // Reset all data to defaults
    reset: () => {
        colorImageData = {};
        colorSizeData = {};
        colorSizeStockData = {};
        defaultImageData = {};
        currentImageIndex = 0;
        currentColor = 'Black';
        selectedCategory = null;
        availableColors = ['Black'];
        window.AppState.initialize();
    }
};

// Add direct property access for easier use
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