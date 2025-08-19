# Product List CSS Organization

## Overview
The product list page styles have been organized into modular components for better maintainability and development experience.

## File Structure

### Main Files
- `product-list.css` - Master import file containing all component imports
- `product-list-index.css` - Alternative master index file

### Component Files (in `/product list/` directory)

#### 1. `product-list-layout.css`
**Purpose**: Layout fundamentals, grid systems, and main containers
**Contains**:
- Global link styles
- Main page layout structure
- Product listing grid system
- Product showcase container
- Grid fallbacks for older browsers

#### 2. `product-list-header.css`
**Purpose**: Page headers, titles, controls, and wall header sections
**Contains**:
- Product page header styling
- Title and subtitle formatting
- Header control buttons (filter, sort)
- Wall header navigation
- Dropdown controls

#### 3. `product-list-sidebar.css`
**Purpose**: Sidebar layout, categories, filters, and navigation
**Contains**:
- Sidebar positioning and layout
- Category navigation styling
- Filter groups and controls
- Trigger content and expandable sections
- Category scrolling behavior

#### 4. `product-list-cards.css`
**Purpose**: Product card layouts, styling, and hover effects
**Contains**:
- Content card base styling
- Product card structure
- Image loading and lazy loading states
- Card overlay effects
- Product information layout (title, price, messaging)
- Colorway buttons and product counts

#### 5. `product-list-banner.css`
**Purpose**: Banner slider, controls, and promotional content
**Contains**:
- Banner container layout
- Slider controls and navigation buttons
- Banner content (titles, subtitles)
- Promotional message styling

#### 6. `product-list-stories.css`
**Purpose**: Related stories section and carousel
**Contains**:
- Stories section layout
- Carousel navigation controls
- Story slide styling
- Story content (categories, titles)
- Carousel interaction states

#### 7. `product-list-responsive.css`
**Purpose**: All responsive breakpoints and mobile optimizations
**Contains**:
- Desktop breakpoints (1200px+, 1024px-1199px)
- Tablet breakpoints (768px-1023px, 600px-767px)
- Mobile breakpoints (480px-599px, 320px-479px, <320px)
- Navigation responsive behavior
- Mobile-specific interactions
- Touch optimizations
- Accessibility improvements
- Print optimizations

## Breakpoint Strategy

### Desktop
- **1440px+**: Full desktop experience
- **1200px-1439px**: Large desktop with adjusted spacing
- **1024px-1199px**: Standard desktop with reduced sidebar

### Tablet
- **768px-1023px**: Tablet landscape with stacked sidebar
- **600px-767px**: Tablet portrait with horizontal categories

### Mobile
- **480px-599px**: Large mobile with 2-column grid
- **320px-479px**: Standard mobile with responsive adjustments
- **280px-319px**: Small mobile with single column

### Special Breakpoints
- **1180px**: Navigation collapse point
- **900px**: Additional tablet optimization
- **360px**: Single column product grid

## Usage Examples

### In HTML
```html
<!-- Option 1: Use main file -->
<link rel="stylesheet" href="styles/product list/product-list.css">

<!-- Option 2: Use index file -->
<link rel="stylesheet" href="styles/product-list-index.css">
```

### In CSS
```css
/* Import all product list styles */
@import 'product list/product-list.css';

/* Or import individual components as needed */
@import 'product list/product-list-layout.css';
@import 'product list/product-list-cards.css';
```

## Development Guidelines

### Adding New Styles
1. **Layout changes**: Add to `product-list-layout.css`
2. **Header modifications**: Add to `product-list-header.css`
3. **Card styling**: Add to `product-list-cards.css`
4. **Responsive changes**: Add to `product-list-responsive.css`

### Mobile-First Approach
- Base styles target mobile devices
- Use `@media (min-width: ...)` for larger screens
- Touch-friendly interactions prioritized

### Performance Considerations
- CSS Grid with flexbox fallback
- Hardware acceleration for animations
- Optimized scroll behavior
- Lazy loading support for images

### Accessibility Features
- Reduced motion support
- High contrast mode compatibility
- Touch target sizing (44px minimum)
- Screen reader friendly structure

## Browser Support
- Modern browsers with CSS Grid support
- Flexbox fallback for older browsers
- Progressive enhancement approach
- Print stylesheet optimization

## Maintenance Notes
- Each component is self-contained
- CSS custom properties used throughout
- Consistent naming conventions
- Comprehensive responsive coverage
