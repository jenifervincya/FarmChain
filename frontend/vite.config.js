import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Dev server proxies /api and /ai to Backend's Spring Cloud Gateway.
// This mirrors production routing (Frontend only ever calls Backend,
// which proxies AI endpoints internally) without hardcoding a host here.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: process.env.VITE_BACKEND_URL || 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
