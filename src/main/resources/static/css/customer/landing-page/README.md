# Landing Page Components

This folder contains the CSS stylesheets for the landing page components of the Nike-inspired e-commerce website.

## Overview

The landing page is built with modular CSS components that create an engaging shopping experience. Each CSS file handles specific sections and functionality to ensure maintainable and organized code.

## Components

### 🎬 Hero Section (`hero.css`)
The main banner area that captures user attention with:
- **Video Background**: Full-screen video placeholder (90vh height)
- **Hero Banner**: Centered content with custom Hildera font
- **Brand Logo**: Positioned Nike-style logo integration
- **Call-to-Action**: Primary action button
- **Typography**: Large display text (200px) in brand red color

**Key Features:**
- Custom font integration (`Hildera.otf`)
- Responsive video background
- Brand color integration
- Centered layout design

### 🌟 Featured Section (`featured.css`)
Showcases featured products and categories:
- **Grid Layout**: 2x2 responsive grid system (4:3 aspect ratio)
- **Image Overlays**: Gradient overlays for text readability
- **Interactive Cards**: Hover effects and cursor pointer
- **Content Positioning**: Absolute positioned content at bottom

**Key Features:**
- CSS Grid implementation
- Gradient overlays
- Responsive image handling
- Interactive hover states

### 🏃‍♂️ Shop by Sport (`shop-by-sport.css`)
Sport category navigation section:
- **Section Header**: Title with navigation controls
- **Carousel Controls**: Previous/next navigation buttons
- **Sport Cards**: Individual sport category displays
- **Navigation**: Circular button controls (48px diameter)

**Key Features:**
- Flexible header layout
- Carousel navigation system
- Consistent button styling
- Responsive spacing

### 🎯 Shop Icons (`shop-icons.css`)
Icon-based product categories:
- **Icon Cards**: Minimum width 300px cards
- **Carousel Track**: Horizontal scrolling with smooth transitions
- **Wrapper Container**: Overflow hidden for clean presentation
- **Responsive Design**: Flexible gap spacing

**Key Features:**
- Horizontal scrolling carousel
- Fixed card dimensions
- Smooth CSS transitions
- Overflow management

### 🏃 Shop Running (`shop-running.css`)
Running-specific product showcase:
- **Large Cards**: 600px flex-basis cards
- **Product Track**: Horizontal product display
- **Interactive Elements**: Cursor pointer for engagement
- **Transition Effects**: 0.3s ease transitions

**Key Features:**
- Large product cards
- Horizontal product scrolling
- Interactive cursor states
- Smooth animations

### 🛒 Cart Sidebar (`cart-sidebar.css`)
Shopping cart functionality:
- **Fixed Positioning**: Right-side slide-out cart (400px width)
- **Cart Counter**: Circular notification badge
- **Product Management**: Add/remove/quantity controls
- **Total Calculations**: Dynamic price calculations
- **Responsive Design**: Full height (100vh) sidebar

**Key Features:**
- Slide-out animation
- Product quantity controls
- Price calculation display
- Empty cart states
- Remove item functionality

### 📱 Responsive Design (`landing-page-responsive.css`)
Mobile and tablet optimizations for all components across different screen sizes.

## File Structure

```
landing-page/
├── README.md                      # This documentation file
├── hero.css                       # Hero section styles
├── featured.css                   # Featured products grid
├── shop-by-sport.css             # Sport categories carousel
├── shop-icons.css                # Icon-based navigation
├── shop-running.css              # Running products section
├── cart-sidebar.css              # Shopping cart sidebar
└── landing-page-responsive.css   # Mobile/tablet responsive styles
```

## CSS Variables Used

The components utilize CSS custom properties (variables) for consistency:

- `--color-brand-red`: Primary brand color
- `--color-brand-orange`: Secondary brand color
- `--color-white`: White color
- `--font-weight-bold`: Bold font weight
- `--font-size-*`: Various font sizes (xs, s, m, l, xl)
- `--size-spacing-*`: Consistent spacing values
- `--transition-duration-normal`: Standard transition timing

## Dependencies

- **Hildera Font**: Custom font family loaded from `../font/hildera/Hildera.otf`
- **Base CSS**: Inherits variables from parent stylesheets
- **Responsive Framework**: Works with the main responsive system

## Usage

These CSS files should be included in the following order:
1. Base styles and variables
2. Component-specific styles (hero, featured, shop-*)
3. Cart sidebar styles
4. Responsive overrides

## Browser Support

- Modern browsers with CSS Grid support
- CSS Custom Properties support
- CSS Flexbox support
- CSS Transitions support

## Maintenance Notes

- Each component is self-contained for easy maintenance
- Consistent naming conventions used throughout
- Modular structure allows for easy updates
- Responsive design considerations included

---

*Last updated: August 13, 2025*