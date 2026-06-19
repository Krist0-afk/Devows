import React, { useState, useEffect } from 'react';
import { fetchWithAuth } from '../utils/api';
import { HeartHandshake, MapPin } from 'lucide-react';
import { IMAGES } from '../constants';

const SUCCESS_STORIES = [
  {
    id: 1,
    title: "Luna volvió a casa",
    timeAgo: "Hace 1 semana",
    content: "\"Luna se escapó por la puerta trasera. Gracias a la alerta inmediata en la aplicación, un vecino a 3 cuadras la reconoció por su collar rojo y nos contactó en menos de 2 horas. ¡Estamos eternamente agradecidos!\"",
    family: "Familia Martinez",
    location: "Zona Sur",
    image: IMAGES.petLuna
  },
  {
    id: 2,
    title: "Rocky fue encontrado",
    timeAgo: "Hace 2 semanas",
    content: "\"Estábamos desesperados cuando Rocky se perdió en el parque. El mapa de avistamientos nos ayudó a rastrear su ruta y finalmente lo encontramos cerca de la avenida principal. ¡Gracias a todos los que reportaron!\"",
    family: "Carlos Gómez",
    location: "Centro",
    image: IMAGES.petRocky
  },
  {
    id: 3,
    title: "Milo está a salvo",
    timeAgo: "Hace 3 semanas",
    content: "\"Milo es un gato muy asustadizo y se escondió en el garaje de un vecino. La notificación de mascota perdida hizo que el vecino revisara y lo encontrara. ¡El pasaporte digital fue clave para confirmar que era él!\"",
    family: "Ana Silva",
    location: "Barrio Norte",
    image: IMAGES.petMilo
  }
];

export const SuccessStoriesView = () => {
  const [stories, setStories] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStories = async () => {
      try {
        const response = await fetchWithAuth('/api/success-stories');
        if (response.ok) {
          const data = await response.json();
          setStories(data);
        }
      } catch (err) {
        console.error('Error fetching stories:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchStories();
  }, []);

  return (
    <div className="flex-1 bg-surface-low py-12 px-6">
      <div className="max-w-4xl mx-auto">
        <div className="text-center mb-16">
          <div className="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-6 text-primary">
            <HeartHandshake size={32} />
          </div>
          <h1 className="text-4xl font-serif font-bold text-text mb-4">Historias de Éxito</h1>
          <p className="text-text-variant text-lg max-w-2xl mx-auto">
            Cada reencuentro es una victoria para nuestra comunidad. Lee cómo la tecnología y la colaboración ciudadana hacen la diferencia.
          </p>
        </div>

        {loading ? (
          <div className="flex flex-col items-center justify-center py-20 space-y-4">
            <div className="w-12 h-12 border-4 border-primary/30 border-t-primary rounded-full animate-spin"></div>
            <p className="text-text-muted font-medium animate-pulse">Cargando historias felices...</p>
          </div>
        ) : (
          <div className="space-y-12 relative before:absolute before:inset-0 before:ml-5 before:-translate-x-px md:before:mx-auto md:before:translate-x-0 before:h-full before:w-0.5 before:bg-gradient-to-b before:from-transparent before:via-outline/30 before:to-transparent">
            {stories.map((story) => (
              <div key={story.id} className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group is-active">
                <div className="flex items-center justify-center w-10 h-10 rounded-full border-4 border-surface-low bg-primary text-surface shadow shrink-0 md:order-1 md:group-odd:-translate-x-1/2 md:group-even:translate-x-1/2 z-10">
                  <HeartHandshake size={16} />
                </div>
                <div className="w-[calc(100%-4rem)] md:w-[calc(50%-2.5rem)] bg-surface p-6 rounded-2xl shadow-sm border border-outline/10 hover:shadow-md transition-shadow">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="font-serif font-bold text-xl text-text">{story.title}</h3>
                    <span className="text-xs font-medium text-primary bg-primary/10 px-3 py-1 rounded-full">{story.timeAgo}</span>
                  </div>
                  <p className="text-text-variant text-sm leading-relaxed mb-4">
                    {story.content}
                  </p>
                  <div className="flex items-center gap-4 pt-4 border-t border-outline/10">
                    <img src={story.image} alt={story.family} className="w-10 h-10 rounded-full object-cover" referrerPolicy="no-referrer" />
                    <div>
                      <p className="text-sm font-bold text-text">{story.family}</p>
                      <p className="text-xs text-text-muted flex items-center gap-1"><MapPin size={12} /> {story.location}</p>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
