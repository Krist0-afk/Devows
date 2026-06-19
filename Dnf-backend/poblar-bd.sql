TRUNCATE TABLE login_attempts, historias, coincidencias, mascotas, usuarios
RESTART IDENTITY CASCADE;

-- =====================================================================
-- 1) USUARIOS
--    Campos: id, name, email, password, rol, avatar, activo, intentos_fallidos
--    Roles válidos según enum Rol: USER, ADMIN, MODERATOR
-- =====================================================================
INSERT INTO usuarios (name, email, password, rol, avatar, activo, intentos_fallidos) VALUES
('Juan Pérez',   'juan.perez@example.com',   '$2a$10$FIsgQkqGfYxDVypPdyjBrekRvRLfI8toXXQmCcJXdftSNDQW.0cDu', 'USER',      'https://api.dicebear.com/7.x/avataaars/svg?seed=Juan',   TRUE, 0),
('María López',  'maria.lopez@example.com',  '$2a$10$FIsgQkqGfYxDVypPdyjBrekRvRLfI8toXXQmCcJXdftSNDQW.0cDu', 'MODERATOR', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Maria',  TRUE, 0),
('Carlos Ruiz',  'carlos.ruiz@example.com',  '$2a$10$FIsgQkqGfYxDVypPdyjBrekRvRLfI8toXXQmCcJXdftSNDQW.0cDu', 'USER',      'https://api.dicebear.com/7.x/avataaars/svg?seed=Carlos', TRUE, 0),
('Ana Gómez',    'ana.gomez@example.com',    '$2a$10$FIsgQkqGfYxDVypPdyjBrekRvRLfI8toXXQmCcJXdftSNDQW.0cDu', 'USER',      'https://api.dicebear.com/7.x/avataaars/svg?seed=Ana',    TRUE, 0),
('Admin DNF',    'admin@dnf.cl',             '$2b$10$VHSKqLcBSglspNMbhnLlku.kCqJXCY.vxXU.gIsT/Lz.r7J.PaeQ2', 'ADMIN',     'https://api.dicebear.com/7.x/avataaars/svg?seed=Admin',  TRUE, 0);

-- =====================================================================
-- 2) MASCOTAS
--    Campos: id, name, type, breed, color, gender, status, location,
--            latitude, longitude, description, image, created_at, usuario_id
--    type   ∈ {dog, cat, bird, other}
--    status ∈ {lost, found}
--    Coordenadas reales de Puerto Montt
-- =====================================================================
INSERT INTO mascotas (name, type, breed, color, gender, status, location, latitude, longitude, description, image, created_at, usuario_id) VALUES
('Max',       'dog',  'Golden Retriever', 'Dorado',            'Macho',  'lost',  'Plaza de Armas, Puerto Montt',     -41.4693, -72.9424, 'Perro muy amigable, se perdió cerca de la plaza central. Lleva collar azul con placa.',     'https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&q=80&w=400', NOW() - INTERVAL '2 days',  1),
('Luna',      'cat',  'Siamés',           'Blanco y Marrón',  'Hembra', 'lost', 'Plaza de Armas, Puerto Montt',            -41.4696, -72.9416, 'Gatita asustada encontrada cerca de la plaza. Es muy cariñosa.',                  'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&q=80&w=400', NOW() - INTERVAL '1 day',   2),
('Rocky',     'dog',  'Bulldog Francés',  'Marrón',           'Macho',  'lost',  'Costanera, Puerto Montt',          -41.4725, -72.9360, 'Llevaba un collar rojo. Visto por el paseo costero por última vez el sábado.',                'https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&q=80&w=400', NOW() - INTERVAL '3 days',  3),
('Coco',      'bird', 'Loro Tricahue',    'Verde',            'Macho',  'lost',  'Mirador Tenglo, Puerto Montt',     -41.4900, -72.9550, 'Loro muy hablador, sabe decir "Hola Coco". Voló desde una ventana abierta.',                 'https://images.unsplash.com/photo-1552728089-57169ab00a09?auto=format&fit=crop&q=80&w=400', NOW() - INTERVAL '5 days',  4),
('Toby',      'dog',  'Mestizo',          'Negro con blanco', 'Macho',  'found', 'Parque Chinquihue, Puerto Montt',   -41.4750, -72.9280, 'Perrito encontrado en las cercanías del parque. Muy sociable y juicioso.',                  'https://images.unsplash.com/photo-1543466835-00a7907e9de1?auto=format&fit=crop&q=80&w=400', NOW() - INTERVAL '12 hours', 2),
('Mishi',     'cat',  'Persa',            'Gris',             'Hembra', 'lost',  'Mall Paseo Costanera, Pto. Montt', -41.4738, -72.9385, 'Gata persa de ojos azules, se asustó con fuegos artificiales durante Año Nuevo.',            'https://images.unsplash.com/photo-1573865526739-10659fec78a5?auto=format&fit=crop&q=80&w=400', NOW() - INTERVAL '6 days',  1);

-- =====================================================================
-- 3) COINCIDENCIAS
--    Campos: id, especie_buscada, color_buscado, estado
--    estado ∈ {activa, inactiva, cerrada}
-- =====================================================================
INSERT INTO coincidencias (especie_buscada, color_buscado, estado) VALUES
('dog',  'Dorado',            'activa'),
('cat',  'Blanco y Marrón',   'activa'),
('bird', 'Verde',             'inactiva'),
('dog',  'Negro con blanco',  'activa'),
('cat',  'Gris',              'cerrada');

-- =====================================================================
-- 4) HISTORIAS (Comunidad)
--    Campos: id, title, family, location, content, image, time_ago
-- =====================================================================
INSERT INTO historias (title, family, location, content, image, time_ago) VALUES
('Reencuentro feliz de Max',     'Familia Pérez',        'Parque Central, Puerto Montt', 'Después de 3 días de intensa búsqueda y gracias a una alerta en DNF, finalmente encontramos a Max. La comunidad se volcó a ayudarnos y un vecino lo reconoció por la foto. ¡Gracias totales!',   'https://images.unsplash.com/photo-1544568100-847a948585b9?auto=format&fit=crop&q=80&w=400', 'Hace 2 días'),
('Luna encontró un hogar',       'Refugio Esperanza',    'Barrio Sur, Puerto Montt',    'Luna fue adoptada hoy por la familia Contreras. Después de semanas en el refugio, finalmente tiene un hogar lleno de amor y un gran patio donde jugar. Le deseamos lo mejor.',                  'https://images.unsplash.com/photo-1543852786-1cf6624b9987?auto=format&fit=crop&q=80&w=400', 'Hace 1 semana'),
('Milo vuelve a casa',           'Familia Gómez',        'Zona Este, Puerto Montt',     'Pensamos que no lo volveríamos a ver, pero apareció gracias a la publicación en la aplicación. Estamos profundamente agradecidos de la comunidad DNF.',                                  'https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?auto=format&fit=crop&q=80&w=400', 'Hace 3 horas'),
('Toby ya está con su familia',  'Familia Contreras',    'Parque Chinquihue',           'Reportamos a Toby como encontrado y en menos de 24 horas apareció su dueño. Nos llenó el corazón verlo regresar a casa tan feliz.',                                                       'https://images.unsplash.com/photo-1450778869180-41d0601e046e?auto=format&fit=crop&q=80&w=400', 'Hace 5 días');

-- =====================================================================
-- 5) LOGIN_ATTEMPTS (auditoría de seguridad)
--    Campos: id, usuario_id, intento_exitoso, fecha_intento, direccion_ip, razon_fallo
-- =====================================================================
INSERT INTO login_attempts (usuario_id, intento_exitoso, fecha_intento, direccion_ip, razon_fallo) VALUES
(1, TRUE,  NOW() - INTERVAL '3 days',  '192.168.1.10',  NULL),
(1, FALSE, NOW() - INTERVAL '4 days',  '192.168.1.10',  'Contraseña incorrecta'),
(2, TRUE,  NOW() - INTERVAL '2 days',  '192.168.1.11',  NULL),
(3, FALSE, NOW() - INTERVAL '1 day',   '200.54.12.45',  'Cuenta no encontrada'),
(4, TRUE,  NOW() - INTERVAL '6 hours', '190.22.4.78',   NULL),
(5, TRUE,  NOW() - INTERVAL '30 min',  '127.0.0.1',     NULL);
