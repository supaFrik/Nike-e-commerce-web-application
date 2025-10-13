# Authentication CSS Structure

This folder contains the organized authentication page styles, broken down into logical components for better maintainability.

## File Structure

```
css/auth/            
├── auth.css               # Base styles & resets & imported files
├── auth-layout.css        # Container & image section layout  
├── auth-header.css        # Logo, title & subtitle styles
├── auth-forms.css         # Form inputs, validation & messages
├── auth-buttons.css       # Buttons, social login & actions
└── auth-animations.css    # Animations & accessibility features
```

## Usage Options

### Option 1: Import All (Recommended)
```html
<link rel="stylesheet" href="styles/base.css">
<link rel="stylesheet" href="styles/auth/auth.css">
```

### Option 2: Import Individual Files
```html
<link rel="stylesheet" href="styles/base.css">
<link rel="stylesheet" href="styles/auth/auth-layout.css">
<link rel="stylesheet" href="styles/auth/auth-header.css">
<link rel="stylesheet" href="styles/auth/auth-forms.css">
<link rel="stylesheet" href="styles/auth/auth-buttons.css">
<link rel="stylesheet" href="styles/auth/auth-animations.css">
```

## Components Breakdown

### 🎨 Layout (`auth-layout.css`)
- Main container layout
- Form section & image section
- Background images & overlays
- Nike logo positioning
- Responsive layout switching

### 📝 Header (`auth-header.css`)
- Nike logo
- Page title & subtitle
- Responsive typography

### 📋 Forms (`auth-forms.css`)
- Input fields & labels
- Password toggle functionality
- Error & success messages
- Form validation styles
- Accessibility features

### 🔘 Buttons (`auth-buttons.css`)
- Primary action buttons
- Social login buttons
- Loading states
- Footer links
- Responsive button sizing

### ✨ Animations (`auth-animations.css`)
- Keyframe animations
- Loading spinners
- Reduced motion support
- Accessibility considerations

## Features

✅ **Modular Structure** - Easy to maintain and update
✅ **Responsive Design** - Mobile-first approach
✅ **Accessibility** - High contrast & reduced motion support
✅ **Loading States** - Built-in loading animations
✅ **Error Handling** - Form validation styling
✅ **Social Login** - Styled social media buttons
✅ **Clean Architecture** - Separated concerns
