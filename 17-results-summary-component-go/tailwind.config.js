/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./**/*.gohtml"],
  theme: {
    extend: {
      colors: {
        'light-slate-blue': 'hsl(252, 100%, 67%)',
        'light-royal-blue': 'hsl(241, 81%, 54%)',
        'violet-blue': 'hsla(256, 72%, 46%)',
        'persian-blue': 'hsla(241, 72%, 46%)',
        'pale-blue': 'hsl(221, 100%, 96%)',
        'light-lavender': 'hsl(241, 100%, 89%)',
        'dark-gray-blue': 'hsl(224, 30%, 27%)',
        'summary-item-color': 'hsl(var(--summary-item-color-var) / <alpha-value>)',
      },
      'fontFamily': {
        'sans': ['HankenGrotesk', 'sans-serif'],
        'added': ['HankenGrotesk', 'sans-serif'],
      },
    },
  },
  plugins: [],
}

