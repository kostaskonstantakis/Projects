module.exports = {
  content: ['./index.html', './js/**/*.js'],
  theme: {
    extend: {
      screens: {
        xs: '480px',
        sm: '640px',
        md: '768px',
        lg: '1024px',
        xl: '1280px',
        '2xl': '1440px',
        '3xl': '1920px',
        '4xl': '2560px'
      },
      colors: {
        brand: {
          50: '#eef4ff',
          100: '#dfeaff',
          500: '#4f46e5',
          600: '#4338ca',
          700: '#3730a3'
        }
      }
    }
  },
  plugins: []
};