# Sanos y Salvos - Aplicación Frontend

## Introducción

Sanos y Salvos (anteriormente conocido como DNF - Dogs n' Friends) es una aplicación web moderna diseñada para ayudar a las familias a reencontrarse con sus mascotas perdidas. La plataforma combina la vigilancia comunitaria con herramientas avanzadas de geolocalización para asegurar que ninguna mascota pase mucho tiempo lejos de casa.

Este repositorio contiene la aplicación frontend, construida con un stack moderno de React, priorizando el rendimiento, el diseño responsivo y la experiencia del usuario. Se integra de manera fluida con los microservicios del backend para proporcionar reportes en tiempo real, mapas interactivos y características enfocadas en la comunidad.

## Características Principales

- **Reporte Detallado de Mascotas Perdidas**: Proporciona un formulario completo que permite a los usuarios enviar reportes detallados. Esto incluye la subida de múltiples fotografías, descripción de características médicas o distintivas, y la ubicación exacta de la desaparición utilizando una interfaz de mapa interactivo.
- **Mapa Interactivo de Avistamientos**: Integra Leaflet y OpenStreetMap para la visualización en tiempo real de mascotas perdidas y avistamientos recientes dentro de un área geográfica específica. Los usuarios pueden rastrear posibles avistamientos y coordinar esfuerzos de búsqueda directamente desde el mapa.
- **Sistema de Notificaciones**: Mantiene a los usuarios informados sobre posibles coincidencias, nuevos avistamientos, comentarios en sus reportes y otras actualizaciones relevantes de la comunidad.
- **Historias de Éxito**: Una sección dedicada donde los usuarios pueden compartir sus historias de reencuentro, fomentando la esperanza y motivando a la comunidad a continuar sus esfuerzos colaborativos.
- **Gestión de Perfil de Usuario**: Perfiles de usuario seguros que permiten a las personas administrar sus reportes activos, configurar preferencias de notificación y revisar su historial de mascotas encontradas.
- **Diseño Premium y Responsivo**: Construido con una interfaz de usuario sofisticada utilizando una paleta de colores personalizada "Sandstone". La aplicación es completamente responsiva, asegurando una experiencia óptima en computadoras de escritorio, tabletas y dispositivos móviles.

## Stack Tecnológico

- **Framework**: React (versión 18 o superior) utilizando TypeScript para comprobación robusta de tipos y mejor experiencia de desarrollo.
- **Estilos**: Tailwind CSS (versión 4.0) para un diseño basado en utilidades e implementación del sistema de diseño personalizado.
- **Enrutamiento**: Enrutamiento del lado del cliente gestionado internamente con un enrutador basado en estado.
- **Integración de Mapas**: Leaflet junto con React-Leaflet para renderizar mapas dinámicos e interactivos.
- **Animaciones**: Framer Motion (a través de motion/react) para proporcionar micro-interacciones suaves y transiciones de página.
- **Iconografía**: Lucide React para íconos vectoriales consistentes y escalables.
- **Herramienta de Construcción**: Vite, proporcionando un servidor de desarrollo rápido y optimizado, así como un proceso de construcción para producción.
- **Pruebas**: Vitest y React Testing Library para pruebas unitarias y de integración exhaustivas.

## Estructura del Proyecto

El código base está organizado de la siguiente manera para mantener la separación de responsabilidades y la escalabilidad:

```text
/src
  /__tests__      # Pruebas unitarias y de integración para componentes y vistas
  /components     # Componentes de interfaz de usuario reutilizables (Navbar, Footer, NotificationMenu, etc.)
  /constants      # Constantes globales, valores de configuración y referencias a activos estáticos
  /lib            # Utilidades compartidas y funciones de ayuda
  /utils          # Ayudantes de comunicación con la API y utilidades de autenticación
  /views          # Vistas/páginas principales de la aplicación (Home, Report, Map, Auth, UserProfile, etc.)
  App.tsx         # Componente raíz responsable de la gestión de estado y orquestación de vistas
  index.css       # Hoja de estilos global, directivas de Tailwind y configuración del tema
  main.tsx        # Punto de entrada de la aplicación
/public           # Activos públicos estáticos (imágenes, favicons)
ENDPOINTS.md      # Documentación de los endpoints de la API REST y detalles de integración
```

## Configuración e Instalación

Sigue estas instrucciones para configurar el proyecto localmente.

### Requisitos Previos

- Node.js (se recomienda la versión 18.x o superior)
- Gestor de paquetes npm o yarn

### Pasos de Instalación

1. Clona el repositorio y navega al directorio del proyecto.
2. Instala las dependencias requeridas:
   ```bash
   npm install
   ```

### Ejecutar el Servidor de Desarrollo

Inicia el servidor de desarrollo de Vite ejecutando:
```bash
npm run dev
```
Una vez que el servidor esté en funcionamiento, puedes acceder a la aplicación en tu navegador en `http://localhost:3000` (o el puerto especificado en tu terminal).

### Construcción para Producción

Para crear una construcción optimizada para producción:
```bash
npm run build
```
Los archivos compilados se generarán en el directorio `dist`, listos para ser servidos por cualquier servicio de alojamiento de archivos estáticos.

### Ejecutar Pruebas

Para ejecutar la suite de pruebas:
```bash
npm run test
```

## Autenticación y Credenciales de Prueba

La aplicación se conecta a una API backend que requiere autenticación para ciertas acciones (como crear un reporte o ver el perfil de usuario). Para propósitos de prueba durante el desarrollo, puedes utilizar las siguientes credenciales preestablecidas:

- **Correo electrónico**: admin@dnf.cl
- **Contraseña**: Admin123!

Alternativamente, puedes registrar una nueva cuenta a través de la vista de registro de la aplicación.

## Integración con la API

El frontend se comunica con un backend basado en microservicios a través de un API Gateway. Todas las solicitudes se enrutan a través de `/api/*`. La autenticación se maneja mediante tokens JWT, que se almacenan en el almacenamiento local (local storage) del navegador y se adjuntan a las solicitudes utilizando la utilidad `fetchWithAuth` ubicada en `src/utils/api.ts`. Para más detalles sobre los endpoints disponibles, consulta el archivo `ENDPOINTS.md`.
