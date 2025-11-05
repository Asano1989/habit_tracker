const {
  iconsPlugin,
  getIconCollections,
} = require("@egoist/tailwindcss-icons");

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/**/*.js",
    "./node_modules/@egoist/tailwindcss-icons/**/*.js"
  ],
  theme: {
  },
  plugins: [
    iconsPlugin({
      // 利用したい icon collection を利用する
      // https://icones.js.org/
      collections: getIconCollections(["tabler", "lucide", "mdi", "ph", "simple-icons"]),
    }),
  ],
}