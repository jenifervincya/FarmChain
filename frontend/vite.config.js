import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

// Dev server proxies /api/v1 to Backend's API Gateway. Reads
// VITE_BACKEND_URL from .env via loadEnv — process.env alone does NOT
// pick up .env file values inside this config file, only actual shell
// env vars, so this was silently falling back to the wrong default
// before. Confirmed backend address (Jenifer, 2026-08-13): localhost:8090.
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    plugins: [react()],
    server: {
      port: 5173,
      proxy: {
        '/api': {
          target: env.VITE_BACKEND_URL || 'http://localhost:8090',
          changeOrigin: true,
        },
      },
    },
  };
});
