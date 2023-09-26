/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.html"],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        'dark-gray': 'hsl(0, 0%, 52%)', // light mode input
        'very-light-gray': 'hsl(0, 0%, 98%)', // light mode bg
        'dark-blue': 'hsl(209, 23%, 22%)', // dark mode elements
        'very-dark-blue': 'hsl(207, 26%, 17%)', // dark mode bg
      },
      fontFamily: {
        'sans': ['NunitoSans', 'sans-serif'],
      },
    },
  },
  plugins: [],
}

