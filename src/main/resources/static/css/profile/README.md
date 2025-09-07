# Customer Profile CSS Organization

## Overview
This folder contains modular CSS files for the customer profile and related sections. Each file is dedicated to a specific section for maintainability and scalability.

## File Structure
```
/styles/customer/
├── profile.css
├── interests.css
├── favourites.css
├── benefits.css
├── apps.css
└── README.md
```

## Sections
- `profile.css`: Profile header, avatar, navigation
- `interests.css`: Interests section, tabs, cards
- `favourites.css`: Favourite products section, product cards
- `benefits.css`: Member benefits section, benefit cards
- `apps.css`: Nike apps section, app cards

## Usage Example
```html
<link rel="stylesheet" href="styles/base.css">
<link rel="stylesheet" href="styles/customer/profile.css">
<link rel="stylesheet" href="styles/customer/interests.css">
<link rel="stylesheet" href="styles/customer/favourites.css">
<link rel="stylesheet" href="styles/customer/benefits.css">
<link rel="stylesheet" href="styles/customer/apps.css">
```

## Guidelines
- Add new section styles as separate files for clarity
- Use BEM or clear class naming for maintainability
- Keep layout, color, and spacing consistent with the overall design
- Update this README when adding new files
