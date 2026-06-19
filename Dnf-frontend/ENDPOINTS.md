# Documentación de Endpoints - Comunidad de Mascotas

Este documento detalla la estructura de la API (actualmente simulada en el frontend) necesaria para el funcionamiento de la aplicación.

## 1. Gestión de Mascotas (Pets API)

### Listar Mascotas
Retorna el listado de mascotas perdidas o encontradas según los filtros aplicados.

- **Endpoint:** `GET /api/pets`
- **Query Params:** 
  - `status`: `lost` | `found` (opcional)
  - `type`: `Perro` | `Gato` (opcional)
  - `query`: Texto de búsqueda (opcional)
- **Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Max",
    "type": "Perro",
    "breed": "Beagle",
    "gender": "Macho",
    "status": "lost",
    "location": "Parque Central, Puerto Montt",
    "coordinates": [-41.4693, -72.9424],
    "timeAgo": "Hace 2 horas",
    "image": "https://images.unsplash.com/photo-pet-1.jpg"
  }
]
```

### Reportar Mascota
Crea un nuevo reporte de una mascota desaparecida o avistada.

- **Endpoint:** `POST /api/pets/report`
- **Payload (Request):**
```json
{
  "name": "Milo",
  "type": "dog" | "cat" | "bird" | "other",
  "breed": "Persa",
  "color": "Blanco con manchas",
  "lastSeenLocation": "Avenida Central 123",
  "coordinates": [-41.4700, -72.9350],
  "date": "2024-04-28",
  "time": "14:30",
  "description": "Tiene un collar azul y es muy asustadizo.",
  "images": ["string_base64_or_url_1", "string_base64_or_url_2"]
}
```
- **Respuesta (201 Created):**
```json
{
  "id": 105,
  "status": "success",
  "message": "Reporte creado exitosamente"
}
```

---

## 2. Autenticación (Auth API)

### Iniciar Sesión (Mock)
Autentica al usuario en la plataforma. Actualmente utiliza credenciales fijas para pruebas.

- **Endpoint:** `POST /api/auth/login`
- **Payload (Request):**
```json
{
  "email": "admin@dnf.cl",
  "password": "Admin123!"
}
```
- **Respuesta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1Ni...",
  "user": {
    "id": "u1",
    "name": "Juan Pérez",
    "email": "juan.perez@ejemplo.com",
    "avatar": null
  }
}
```

---

## 3. Notificaciones (Notifications API)

### Listar Notificaciones
Obtiene las notificaciones del usuario autenticado.

- **Endpoint:** `GET /api/notifications`
- **Respuesta (200 OK):**
```json
[
  {
    "id": "1",
    "title": "Posible avistamiento",
    "message": "Alguien cree haber visto a Max...",
    "type": "alert" | "success" | "info",
    "time": "5m",
    "read": false
  }
]
```

### Marcar como Leídas
Marca todas las notificaciones como leídas.

- **Endpoint:** `PATCH /api/notifications/read-all`
- **Respuesta (200 OK):**
```json
{
  "status": "success",
  "message": "Notificaciones marcadas como leídas"
}
```

---

## 4. Historias de Éxito (Success Stories API)

### Listar Historias
Obtiene los relatos de reencuentros exitosos de la comunidad.

- **Endpoint:** `GET /api/success-stories`
- **Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Luna volvió a casa",
    "family": "Familia Martinez",
    "location": "Zona Sur",
    "content": "Luna se escapó por la puerta trasera...",
    "image": "https://images.unsplash.com/photo-pet-luna.jpg",
    "timeAgo": "Hace 1 semana"
  }
]
```

---

## 4. Servicios de Terceros (Externos)

### Búsqueda de Ubicación (Nominatim)
Servicio utilizado para geocodificar las búsquedas del mapa.

- **Endpoint:** `GET https://nominatim.openstreetmap.org/search`
- **Query Params:** 
  - `q`: Texto de búsqueda (ej: "Puerto Montt")
  - `format`: `json`
  - `limit`: `1`
- **Respuesta (200 OK):**
```json
[
  {
    "lat": "-41.4693",
    "lon": "-72.9424",
    "display_name": "Puerto Montt, Región de Los Lagos, Chile",
    "type": "city"
  }
]
```

---

## 5. Recomendaciones (Autocomplete API)

### Sugerencias de Búsqueda
Sugerencias en tiempo real basadas en el texto que el usuario ingresa en los buscadores.

- **Endpoint:** `GET /api/pets/suggestions`
- **Query Params:** 
  - `q`: Caracteres iniciales (ej: "M")
- **Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Max",
    "breed": "Beagle",
    "image": "https://url-pet.jpg",
    "coordinates": [-41.4693, -72.9424],
    "status": "lost"
  }
]
```
