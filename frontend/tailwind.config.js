export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      fontFamily: { sans: ["'DM Sans'", "system-ui", "sans-serif"], mono: ["'JetBrains Mono'", "monospace"] },
      colors: {
        brand: { 50:"#f0f4ff", 100:"#dde7ff", 200:"#c3d2fe", 400:"#7a9ef8", 500:"#4f75f6", 600:"#3b5ef0", 700:"#2d49d4", 800:"#2438a8", 900:"#1e2e80" },
        slate: { 850:"#172033", 950:"#0b1120" }
      },
      boxShadow: {
        glass: "0 4px 24px -2px rgba(0,0,0,.25), inset 0 1px 0 rgba(255,255,255,.08)",
        card:  "0 1px 3px rgba(0,0,0,.08), 0 4px 16px rgba(79,117,246,.06)"
      }
    }
  },
  plugins: []
};
