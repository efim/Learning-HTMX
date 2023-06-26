/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.scala"],
  theme: {
    extend: {
      fontFamily: {
        'sans': ['Karla', 'sans-serif']
      },
      colors: {
        'primary-cyan': 'hsl(179, 62%, 43%)',
        'primary-yellow': 'hsl(71, 73%, 54%)',
        'neutral-gray': 'hsl(204, 43%, 93%)',
        'grayish-blue': 'hsl(218, 22%, 67%)',
        'bg-subscription': 'hsl(179, 61%, 44%)',
        'bg-why-us': 'hsl(179, 47%, 52%)',
      }
    },
  },
  plugins: [],
}

