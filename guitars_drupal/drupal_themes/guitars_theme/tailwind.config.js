/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: ['class', '[data-theme="dark"]'],
  content: [
    "./templates/**/*.html.twig",
    "./js/**/*.js",
    "../../drupal_modules/**/*.php",
    "../../drupal_modules/**/*.html.twig"
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
