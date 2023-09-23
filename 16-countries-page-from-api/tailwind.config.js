/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.html"],
  theme: {
    extend: {
      colors: {
        'dark-gray': 'hsl(0, 0%, 52%)',
        'very-light-gray': 'hsl(0, 0%, 98%)',
      },
      fontFamily: {
        'sans': ['NunitoSans', 'sans-serif'],
      },
    },
  },
  plugins: [],
}

