# Dockerfile para Frontend y BFF (Backend for Frontend)
FROM node:18-alpine

WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias usando npm
RUN npm install

# Copiar el resto del código
COPY . .

# Construir la aplicación Vite
RUN npm run build

# Variables de entorno
ENV NODE_ENV=production
ENV PORT=80

# Exponer el puerto
EXPOSE 80

# Ejecutar el servidor BFF
CMD ["npx", "tsx", "server.ts"]
