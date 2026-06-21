# Dockerfile para Frontend y BFF (Backend for Frontend)
FROM node:18-alpine

WORKDIR /app

# Instalar pnpm
RUN npm install -g pnpm

# Copiar archivos de dependencias
COPY package.json pnpm-lock.yaml ./

# Instalar dependencias
RUN pnpm install

# Copiar el resto del código
COPY . .

# Construir la aplicación Vite
RUN pnpm run build

# Variables de entorno
ENV NODE_ENV=production
ENV PORT=80

# Exponer el puerto
EXPOSE 80

# Ejecutar el servidor BFF
CMD ["npx", "tsx", "server.ts"]
