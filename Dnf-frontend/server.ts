import express from 'express';
import { createServer as createViteServer } from 'vite';
import path from 'path';
import { fileURLToPath } from 'url';
import axios from 'axios';
import dotenv from 'dotenv';

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// --- Configuración y Datos de Respaldo (Fallback) ---
const SPRING_BOOT_URL = process.env.VITE_SPRING_BOOT_API_URL || 'http://localhost:8080';

// Datos Mock (se usarán si el backend no responde o no está configurado)
let mockPets = [
  { id: 1, name: "Max", type: "Perro", breed: "Beagle", gender: "Macho", status: "lost", location: "Costanera, Puerto Montt", coordinates: [-41.4725, -72.9391] as [number, number], timeAgo: "Hace 2 horas", image: "https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?q=80&w=800" },
  { id: 2, name: "Luna", type: "Gato", breed: "Siamés", gender: "Hembra", status: "lost", location: "Plaza de Armas, Puerto Montt", coordinates: [-41.4696, -72.9416] as [number, number], timeAgo: "Hace 5 horas", image: "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?q=80&w=800" },
  { id: 3, name: "Rocky", type: "Perro", breed: "Mestizo", gender: "Macho", status: "lost", location: "Terminal de Buses, Puerto Montt", coordinates: [-41.4745, -72.9348] as [number, number], timeAgo: "Ayer", image: "https://images.unsplash.com/photo-1533738363-b7f9aef128ce?q=80&w=800" },
  { id: 4, name: "Milo", type: "Gato", breed: "Persa", gender: "Macho", status: "lost", location: "Angelmó, Puerto Montt", coordinates: [-41.4839, -72.9554] as [number, number], timeAgo: "Hace 3 días", image: "https://images.unsplash.com/photo-1513245535761-077a2dd8a6f6?q=80&w=800" }
];

let mockNotifications = [
  { id: '1', title: 'Posible avistamiento', message: 'Alguien cree haber visto a Max...', type: 'alert', time: '5m', read: false }
];

const api = axios.create({
  baseURL: SPRING_BOOT_URL,
  timeout: 3000, // Timeout corto para no lentizar la UI si el backend está caído
  headers: { 'Content-Type': 'application/json' }
});

// Función auxiliar para extraer el token
const getAuthHeaders = (req: express.Request) => {
  const authHeader = req.headers.authorization;
  return authHeader ? { Authorization: authHeader } : {};
};

let backendConnected = false;

const handleApiError = (error: any, res: express.Response, fallback: () => void) => {
  if (error.response) {
    backendConnected = true;
    res.status(error.response.status).json(error.response.data);
    return;
  }
  if (backendConnected) {
    res.status(503).json({ message: 'Conexión con el backend perdida.' });
    return;
  }
  fallback();
};

async function startServer() {
  const app = express();
  const PORT = process.env.PORT || 80;

  app.use(express.json({ limit: '50mb' }));
  app.use(express.urlencoded({ limit: '50mb', extended: true }));

  // --- API Routes (BFF Layer con Fallback) ---

  app.get('/api/pets', async (req, res) => {
    try {
      const response = await api.get('/api/pets', { 
        params: req.query,
        headers: getAuthHeaders(req)
      });
      backendConnected = true;
      res.json(response.data);
    } catch (error: any) {
      handleApiError(error, res, () => {
        console.warn(`[BFF] Error en /api/pets: ${error.message}. Usando Mock Data.`);
        let filteredPets = [...mockPets];
        if (req.query.status) {
          filteredPets = filteredPets.filter(p => p.status === req.query.status);
        }
        if (req.query.query) {
          const q = String(req.query.query).toLowerCase();
          filteredPets = filteredPets.filter(p => p.name.toLowerCase().includes(q) || p.breed.toLowerCase().includes(q) || p.type.toLowerCase().includes(q));
        }
        res.json(filteredPets);
      });
    }
  });

  app.get('/api/pets/suggestions', async (req, res) => {
    try {
      const response = await api.get('/api/pets/suggestions', { 
        params: req.query,
        headers: getAuthHeaders(req)
      });
      backendConnected = true;
      res.json(response.data);
    } catch (error: any) {
      handleApiError(error, res, () => {
        const q = String(req.query.q || '').toLowerCase();
        const suggestions = mockPets.filter(p => p.name.toLowerCase().includes(q));
        res.json(suggestions);
      });
    }
  });

  app.get('/api/pets/:id', async (req, res) => {
    try {
      const response = await api.get(`/api/pets/${req.params.id}`, { 
        headers: getAuthHeaders(req)
      });
      backendConnected = true;
      res.json(response.data);
    } catch (error: any) {
      handleApiError(error, res, () => {
        console.warn(`[BFF] Error en /api/pets/${req.params.id}: ${error.message}. Usando Mock Data.`);
        const id = parseInt(req.params.id);
        const pet = mockPets.find(p => p.id === id);
        if (pet) {
          res.json(pet);
        } else {
          res.status(404).json({ error: 'Not found' });
        }
      });
    }
  });

  app.post('/api/pets/report', async (req, res) => {
    try {
      const response = await api.post('/api/pets/report', req.body, {
        headers: getAuthHeaders(req)
      });
      backendConnected = true;
      res.status(response.status).json(response.data);
    } catch (error: any) {
      handleApiError(error, res, () => {
        const newPet = { id: mockPets.length + 1, ...req.body, status: 'lost', timeAgo: 'Recién publicado' };
        mockPets.push(newPet);
        res.status(201).json({ id: newPet.id, status: 'success', message: 'Guardado localmente (Backend offline)' });
      });
    }
  });

  app.post('/api/usuarios/login', async (req, res) => {
    try {
      const response = await api.post('/api/usuarios/login', req.body);
      backendConnected = true;
      res.json(response.data);
    } catch (error: any) {
      handleApiError(error, res, () => {
        if (req.body.email === 'admin@test.com' || (req.body.email === 'admin@dnf.cl' && req.body.password === 'Admin123!')) {
          res.json({ token: 'mock-token', user: { id: 1, name: 'Admin (Modo Offline)', email: req.body.email } });
        } else {
          res.status(401).json({ message: 'Modo Offline: Usa admin@dnf.cl / Admin123!' });
        }
      });
    }
  });

  app.get('/api/notifications', async (req, res) => {
    try {
      const response = await api.get('/api/notificaciones', {
        headers: getAuthHeaders(req)
      });
      backendConnected = true;
      res.json(response.data);
    } catch (error: any) {
      handleApiError(error, res, () => {
        res.json(mockNotifications);
      });
    }
  });

  app.get('/api/success-stories', async (req, res) => {
    try {
      const response = await api.get('/api/success-stories', {
        headers: getAuthHeaders(req)
      });
      backendConnected = true;
      res.json(response.data);
    } catch (error: any) {
      handleApiError(error, res, () => {
        res.json([{ id: 1, title: "Historias Offline", content: "El backend no está conectado aún.", image: "https://images.unsplash.com/photo-1543852786-1cf6624b9987?q=80&w=800" }]);
      });
    }
  });

  // --- Vite & Production Setup ---
  if (process.env.NODE_ENV !== 'production') {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: 'spa',
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), 'dist');
    app.use(express.static(distPath));
    app.get('*', (req, res) => {
      res.sendFile(path.join(distPath, 'index.html'));
    });
  }

  app.listen(PORT, '0.0.0.0', () => {
    console.log(`BFF Gateway running on http://localhost:${PORT}`);
    console.log(`Configured to proxy to Spring Boot at: ${SPRING_BOOT_URL}`);
  });
}

startServer();
