# Dockerfile para Frontend y BFF (Backend for Frontend)
# Node 20+ es requerido por @tailwindcss/oxide, @vitejs/plugin-react, vitest y jsdom.
# Usar Node 18 hace que npm omita el binding nativo de @tailwindcss/oxide
# (optional dependency) y el build falla con "Cannot find native binding".
FROM node:20-alpine

WORKDIR /app

# pnpm respeta el pnpm-lock.yaml del repo (instalacion reproducible) y maneja
# mejor las optional dependencies nativas que npm.
RUN npm install -g pnpm@9

# Copiar archivos de dependencias (incluido el lockfile)
COPY package.json pnpm-lock.yaml ./

# Instalar dependencias respetando el lockfile (falla si esta desincronizado,
# evitando drift silencioso entre package.json y el lockfile)
RUN pnpm install --frozen-lockfile

# Copiar el resto del codigo
COPY . .

# Construir la aplicacion Vite
RUN pnpm run build

# Variables de entorno
ENV NODE_ENV=production
ENV PORT=80

# Exponer el puerto
EXPOSE 80

# Ejecutar el servidor BFF
CMD ["pnpm", "exec", "tsx", "server.ts"]
