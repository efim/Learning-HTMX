/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.html"],
  theme: {
    extend: {
      colors: {
        'pale-blue': 'hsl(225, 100%, 94%)',
        'bright-blue': 'hsl(245, 75%, 52%)',
        'very-pale-blue': 'hsl(225, 100%, 98%)',
        'desaturated-blue': 'hsl(224, 23%, 55%)',
        'dark-blue': 'hsl(223, 47%, 23%)',
      },
      fontFamily: {
        'sans': ['Red Hat Display', 'sans-serif'], // This will set Roboto as the default sans font
      },
      fontWeight: {
        'normal': 500,
        'bold': 700,
        'extrabold': 900,
      }

    },
  },
  plugins: [],
}

