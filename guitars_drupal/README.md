# guitars_drupal

Drupal + Tailwind + Docker Desktop starter for the guitars site.

## Start

```bash
docker compose up -d
```

- Drupal: http://localhost:8080
- DB host in installer: `db`
- DB name/user/password: `drupal`

## Theme

Custom theme is at:

`drupal_themes/guitars_theme`

Tailwind source:

`drupal_themes/guitars_theme/tailwind/input.css`

Compiled output (served by Drupal):

`drupal_themes/guitars_theme/css/output.css`

## Tailwind Watch

The `tailwind` container runs:

```bash
npm install && npm run dev
```

so CSS rebuilds automatically when you edit Twig/CSS/JS in the theme.

## After first install

1. Open Drupal admin: `/admin/appearance`
2. Install and set default theme: **Guitars Theme**
3. Clear caches if needed.
