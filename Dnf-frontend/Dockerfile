# Dockerfile para Frontend y BFF (Backend for Frontend)
# Node 20+ es requerido por @tailwindcss/oxide, @vitejs/plugin-react, vitest y jsdom.
# Usar Node 18 hace que npm omita el binding nativo de @tailwindcss/oxide
# (optional dependency) y el build falla con "Cannot find native binding".
FROM node:20-alpine

WORKDIR /app



# Copiar archivos de dependencias
COPY package.json package-lock.yaml* ./

# Instalar dependencias
RUN npm install

# Copiar el resto del codigo
COPY . .

# Construir la aplicacion Vite
RUN npm run build

# Variables de entorno
ENV NODE_ENV=production
ENV PORT=80

# Exponer el puerto
EXPOSE 80

# Ejecutar el servidor BFF
CMD ["npx", "tsx", "server.ts"]
