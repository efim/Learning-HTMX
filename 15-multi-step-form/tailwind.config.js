/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{scala,html}"],
  theme: {
    extend: {
      colors: {
        // ### Primary
        "marine-blue": "hsl(213, 96%, 18%)",
        "purplish-blue": "hsl(243, 100%, 62%)",
        "pastel-blue": "hsl(228, 100%, 84%)",
        "light-blue": "hsl(206, 94%, 87%)",
        "strawberry-red": "hsl(354, 84%, 57%)",

        // ### neutral
        "cool-gray": "hsl(231, 11%, 63%)",
        "light-gray": "hsl(229, 24%, 87%)",
        magnolia: "hsl(217, 100%, 97%)",
        alabaster: "hsl(231, 100%, 99%)",
      },
      backgroundImage: {
        'sidebar-mobile': 'url("images/bg-sidebar-mobile.svg")',
        'sidebar-desktop': 'url("images/bg-sidebar-desktop.svg")',
      },
      fontFamily: {
        'sans': ['Ubuntu', 'sans-serif'], // This will set Roboto as the default sans font
      },
      fontWeight: {
        'normal': 400,
        'semibold': 600,
        'bold': 700,
      },
      width: {
        'desktop-form': '60rem',
      },
      height: {
        'desktop-form': '38rem',
      },
    },
  },
  plugins: [],
};
