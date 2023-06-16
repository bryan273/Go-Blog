/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/components/**/*.{html,js,jsx}",
    "./src/pages/**/*.{html,js,jsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [require("daisyui")],
};
