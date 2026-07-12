# Guitars Drupal Setup Guide

This document explains how to set up and use the guitars_drupal project so that guitars are displayed with the same layout and functionality as the `guitars` and `guitars_tailwind` projects.

## Architecture

The guitars_drupal project is a Drupal-based implementation with:

1. **Guitars Import Module** (`drupal_modules/guitars_import/`)
   - Creates a "Guitar" content type with fields: `field_image`, `field_type`, and `body` (description)
   - Provides an import controller that reads guitar data from `guitars.json`
   - Includes a Drupal view (`views.view.guitars.yml`) that displays all guitars

2. **Guitars Theme** (`drupal_themes/guitars_theme/`)
   - Uses Tailwind CSS with custom styling
   - Includes view template (`views-view-unformatted--guitars.html.twig`) for rendering guitars
   - Loads JavaScript for interactivity (filters, search, orientations, dark mode, lightbox)
   - CSS and JS files are styled identically to the `guitars` project

## Initial Setup

### 1. Install Docker and Start Containers

```bash
cd /path/to/guitars_drupal
docker compose up -d
```

This starts:
- Drupal at `http://localhost:8080`
- MySQL database
- Tailwind CSS compiler

### 2. Install Drupal

Visit `http://localhost:8080` and follow the installer:
- Database host: `db`
- Database name: `drupal`
- Database user: `drupal`
- Database password: `drupal`

### 3. Install and Activate Theme

After Drupal installation:

1. Go to `/admin/appearance`
2. Find "Guitars Theme" in the list
3. Click "Install and set as default"

### 4. Install and Activate Modules

1. Go to `/admin/modules`
2. Enable **Guitars Import** module
3. Save

When enabled, the module will:
- Create the "Guitar" content type with required fields
- Create the "Guitars" view
- Set up the view page at `/guitars`

### 5. Import Guitar Data

1. Go to `/admin/config/guitars/import`
2. Click the form to import guitars from `data/guitars.json`
3. The module will create guitar nodes with:
   - Titles
   - Descriptions
   - Images (automatically copied to Drupal public files directory)
   - Guitar types (electric, bass, acoustic, etc.)

### 6. View the Guitars

Visit `/guitars` to see the guitars display with:
- **Language selector** (English, Ελληνικά, Русский)
- **Search filter**
- **Sort options** (Default, Newest, By Type, A-Z)
- **Orientation controls** (Vertical, Horizontal, Slideshow)
- **Type filter** (All, Electric Guitar, Bass, Acoustic, etc.)
- **Dark mode toggle**
- **Lightbox** for enlarged images

## Guitar Data Format

The `data/guitars.json` file contains guitar data with the following structure:

```json
[
  {
    "title": "Guitar Name",
    "description": "Full description of the guitar",
    "img": "css/media/guitar-image.png",
    "type": "electric"
  }
]
```

### Supported Types

- `electric` - Standard electric guitar
- `electric 7string` - Electric 7-string guitar
- `electric signature` - Electric signature model
- `electric baritone` - Electric baritone guitar
- `bass` - Bass guitar
- `acoustic` - Acoustic guitar
- `ukulele` - Ukulele

## Styling and Customization

### CSS

- **Source**: `drupal_themes/guitars_theme/tailwind/input.css`
- **Compiled**: `drupal_themes/guitars_theme/css/output.css`
- Tailwind CSS is automatically rebuilt when you edit files (via Docker container)

### JavaScript

- **Location**: `drupal_themes/guitars_theme/js/guitars.js`
- Handles dark mode, filters, search, orientations, and lightbox functionality
- Automatically loaded by the theme library

### View Template

- **Location**: `drupal_themes/guitars_theme/templates/views/views-view-unformatted--guitars.html.twig`
- Renders individual guitar nodes with correct CSS classes
- Extracts guitar type, description, and image URL from Drupal entity

## Troubleshooting

### Guitars not displaying
- Check that the "Guitars Theme" is set as default theme
- Ensure the "Guitars Import" module is enabled
- Visit `/admin/config/guitars/import` to import guitar data

### Images not showing
- Verify that `css/media/` directory contains all guitar image files
- Check Drupal file permissions on `public://` directory
- Run the import again to copy images to Drupal public files directory

### View not accessible at `/guitars`
- Clear Drupal caches at `/admin/config/development/performance`
- Ensure the Guitars view is enabled and set to display at page path `/guitars`

### Styles not applying
- Rebuild Tailwind CSS: `docker exec guitars_drupal-tailwind-1 npm run build`
- Clear Drupal theme cache at `/admin/config/development/performance`
- Hard refresh browser cache (Ctrl+Shift+R or Cmd+Shift+R)

## Development

### Watch Tailwind CSS Changes

Tailwind is automatically watched inside Docker. To rebuild manually:

```bash
docker exec guitars_drupal-tailwind-1 npm run build
```

### Modify Guitar Data

1. Edit `drupal_modules/guitars_import/data/guitars.json`
2. Delete existing guitar nodes (or they won't be re-imported due to duplicate checking)
3. Visit `/admin/config/guitars/import` to re-import

### Add New Content Type Fields

1. Edit `guitars_import.install` hook_install() function
2. Add field definitions
3. Uninstall and reinstall the module via `/admin/modules`

## Comparison with Other Projects

All three projects display guitars identically:

- **guitars** - Pure HTML/CSS/JS (no framework)
- **guitars_tailwind** - HTML with Tailwind CSS
- **guitars_drupal** - Drupal with database-backed guitar nodes

All three share:
- Same CSS styling for layouts and animations
- Same JavaScript functionality for filters and interactivity
- Same guitar data and descriptions
- Same view template structure (CSS classes and HTML markup)

## Additional Resources

- [Drupal Views Documentation](https://www.drupal.org/docs/8/core/modules/views)
- [Tailwind CSS Documentation](https://tailwindcss.com/docs)
- [Drupal Theme Development](https://www.drupal.org/docs/8/theming)
