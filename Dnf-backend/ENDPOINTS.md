# Documentación de Endpoints (API REST)

Este documento detalla todos los endpoints expuestos por los distintos microservicios que conforman el backend de **DNF (Dogs n' Friends)**. 

Todas las peticiones deben ser enviadas a través del **API Gateway** corriendo por defecto en `http://localhost:8080`. El Gateway se encargará de enrutar la petición al microservicio correspondiente.

> **Nota:** Todos los microservicios cuentan con documentación interactiva mediante Swagger/OpenAPI. Una vez en ejecución, puedes visitar `http://localhost:<puerto-servicio>/swagger-ui.html` para probarlos gráficamente.

---

## 1. Microservicio: Ms-Usuario
**Ruta Base:** `/api/usuarios`

Gestiona los usuarios, autenticación y perfiles de la plataforma.

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/usuarios` | Registra y crea un nuevo usuario. |
| `GET` | `/api/usuarios` | Obtiene la lista de usuarios. |
| `DELETE` | `/api/usuarios/{id}` | Elimina un usuario especificando su ID. |
| `POST` | `/api/usuarios/login` | Autentica a un usuario y genera la sesión (o JWT). |

---

## 2. Microservicio: Ms-Mascota
**Ruta Base:** `/api/pets`

Gestiona las mascotas reportadas (perdidas o encontradas).

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/pets` | Obtiene una lista de mascotas (puede incluir filtros por query param). |
| `POST` | `/api/pets/report` | Reporta y registra una nueva mascota en el sistema. |
| `GET` | `/api/pets/{id}` | Obtiene los detalles de una mascota por su ID. |
| `GET` | `/api/pets/usuario/{usuarioId}` | Obtiene las mascotas reportadas por un usuario específico. |
| `GET` | `/api/pets/suggestions` | Búsqueda/sugerencias de mascotas mediante el parámetro de consulta `?q=`. |

---

## 3. Microservicio: Ms-Coincidencias
**Ruta Base:** `/api/coincidencias`

Gestión de la lógica de "matches" y coincidencias entre animales y reportes.

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/coincidencias` | Obtiene el listado de coincidencias registradas. |
| `POST` | `/api/coincidencias` | Registra una nueva coincidencia. |
| `GET` | `/api/coincidencias/buscar` | Busca coincidencias basadas en parámetros (comunicándose con Ms-Mascota). |
| `PUT` | `/api/coincidencias/{id}` | Actualiza el estado o información de una coincidencia específica. |
| `DELETE` | `/api/coincidencias/{id}` | Elimina una coincidencia existente. |

---

## 4. Microservicio: Ms-Comunidad
**Ruta Base:** `/api/success-stories`

Manejo de casos de éxito y foro comunitario.

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/success-stories` | Obtiene la lista de todas las historias de éxito o publicaciones. |
| `POST` | `/api/success-stories` | Crea y publica una nueva historia de éxito. |

---

## 5. Microservicio: Ms-Notificaciones
**Ruta Base:** `/api/notificaciones`

Servicio interno para alertas y correos (sin persistencia de DB).

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/notificaciones/enviar-alerta` | Recibe un `AlertaMatchDTO` y envía la notificación correspondiente (e.g. email) a los interesados. |

---
