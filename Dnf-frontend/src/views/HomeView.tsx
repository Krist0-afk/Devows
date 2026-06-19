import React from 'react';
import { Activity, MapPin, Info, Star } from 'lucide-react';
import { IMAGES } from '../constants';

export const HomeView = ({ navigate }: { navigate: (v: string) => void }) => {
  return (
    <div className="flex flex-col w-full">
      {/* Hero Section */}
      <section className="relative min-h-[80vh] flex items-center overflow-hidden bg-surface-low">
        <div className="max-w-7xl mx-auto px-6 grid grid-cols-1 md:grid-cols-2 gap-12 items-center py-20">
          <div className="z-10 space-y-8">
            <h1 className="font-serif text-5xl md:text-6xl font-extrabold text-primary leading-tight">
              Arraigados en la Comunidad: Trayéndolos a casa sanos y salvos.
            </h1>
            <p className="text-xl text-text-variant max-w-lg leading-relaxed">
              Combinamos la vigilancia comunitaria con microservicios avanzados y geolocalización para asegurar que ninguna mascota perdida lo esté por mucho tiempo.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 pt-4">
              <button onClick={() => navigate('report')} className="bg-primary text-surface px-8 py-4 rounded-xl font-bold text-lg hover:shadow-lg transition-all">
                Reportar Mascota Perdida
              </button>
              <button onClick={() => navigate('map')} className="bg-secondary-container text-primary border border-primary/20 px-8 py-4 rounded-xl font-bold text-lg hover:bg-surface-container transition-all">
                Ver Mapa de Avistamientos
              </button>
            </div>
          </div>
          <div className="relative">
            <div className="absolute -inset-4 bg-tertiary-container/20 rounded-full blur-3xl"></div>
            <img 
              className="relative z-10 rounded-2xl shadow-2xl w-full h-[500px] object-cover border-8 border-surface" 
              src={IMAGES.heroDog} 
              alt="Happy golden retriever" 
              referrerPolicy="no-referrer"
            />
          </div>
        </div>
      </section>

      {/* How it Works */}
      <section className="py-24 bg-background">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-16 space-y-4">
            <span className="text-tertiary font-bold tracking-widest uppercase text-sm">Nuestro Ecosistema</span>
            <h2 className="font-serif text-4xl font-bold text-text">Cómo Funciona</h2>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-12 gap-6 md:h-[500px]">
            {/* Card 1 */}
            <div className="md:col-span-7 bg-surface-container rounded-2xl p-8 flex flex-col justify-between shadow-sm border border-outline/10 overflow-hidden relative group">
              <div className="space-y-4 z-10 relative">
                <div className="w-12 h-12 bg-primary/10 rounded-xl flex items-center justify-center">
                  <Activity className="text-primary" size={24} />
                </div>
                <h3 className="font-serif text-2xl font-bold">Coincidencias mediante Microservicios</h3>
                <p className="text-text-variant leading-relaxed text-lg">
                  Nuestro motor de búsqueda avanzado cruza reportes de mascotas perdidas con avistamientos en tiempo real usando algoritmos de redes neuronales.
                </p>
              </div>
              <div className="mt-8 rounded-xl bg-surface/50 h-32 flex items-center justify-center border border-dashed border-primary/30 relative overflow-hidden">
                <div className="absolute inset-0 flex items-center justify-around px-10 opacity-20 group-hover:opacity-40 transition-opacity">
                  {[...Array(6)].map((_, i) => (
                    <div key={i} className="flex flex-col items-center gap-2">
                      <div className="w-3 h-3 bg-primary rounded-full animate-bounce" style={{ animationDelay: `${i * 0.2}s` }}></div>
                      <div className="w-1 h-12 bg-gradient-to-b from-primary to-transparent"></div>
                    </div>
                  ))}
                </div>
                <span className="text-primary font-bold italic z-10">Algoritmo de Coincidencia Activo</span>
              </div>
            </div>
            
            {/* Card 2 */}
            <div className="md:col-span-5 bg-primary text-surface rounded-2xl p-8 flex flex-col justify-center gap-6 shadow-md relative overflow-hidden group">
              <div className="absolute top-0 right-0 w-32 h-32 bg-white/10 rounded-full blur-3xl -mr-10 -mt-10"></div>
              <MapPin size={32} className="text-surface relative z-10" />
              <h3 className="font-serif text-2xl font-bold relative z-10">Rastreo por Geolocalización</h3>
              <p className="opacity-90 leading-relaxed relative z-10">
                Visualiza reportes al instante en nuestro mapa interactivo. Rastrea avistamientos potenciales y coordina esfuerzos de búsqueda.
              </p>
              <div className="mt-4 h-32 bg-surface/10 rounded-xl border border-surface/20 backdrop-blur-sm relative overflow-hidden">
                <img 
                  src={IMAGES.miniMap} 
                  alt="Mini Map" 
                  className="w-full h-full object-cover opacity-50 group-hover:scale-110 transition-transform duration-700" 
                  referrerPolicy="no-referrer"
                />
                <div className="absolute inset-0 flex items-center justify-center">
                  <div className="w-4 h-4 bg-error rounded-full animate-ping"></div>
                </div>
              </div>
            </div>
            
            {/* Card 3 */}
            <div className="md:col-span-12 bg-secondary-container rounded-2xl p-8 flex flex-col md:flex-row items-center gap-8 shadow-sm group">
              <div className="flex-1 space-y-4">
                <div className="w-12 h-12 bg-tertiary/10 rounded-xl flex items-center justify-center">
                  <Info className="text-tertiary" size={24} />
                </div>
                <h3 className="font-serif text-2xl font-bold text-tertiary-container-text">Pasaportes Digitales para Mascotas</h3>
                <p className="text-text-muted leading-relaxed max-w-3xl">
                  Mantén registros estructurados de los identificadores físicos únicos de tu mascota, necesidades médicas y fotos. Estos pasaportes son la herramienta de identificación definitiva.
                </p>
              </div>
              <div className="flex gap-4 relative">
                <div className="w-24 h-32 bg-surface rounded-lg shadow-lg transform -rotate-6 flex flex-col p-2 group-hover:-rotate-12 transition-transform">
                   <img src={IMAGES.petMax} className="w-full h-16 object-cover rounded mb-2" alt="Max" referrerPolicy="no-referrer" />
                   <div className="h-1 w-full bg-outline/20 rounded mb-1"></div>
                   <div className="h-1 w-2/3 bg-outline/20 rounded"></div>
                </div>
                <div className="w-24 h-32 bg-surface rounded-lg shadow-lg transform rotate-3 flex flex-col p-2 group-hover:rotate-6 transition-transform">
                   <img src={IMAGES.petLuna} className="w-full h-16 object-cover rounded mb-2" alt="Luna" referrerPolicy="no-referrer" />
                   <div className="h-1 w-full bg-outline/20 rounded mb-1"></div>
                   <div className="h-1 w-2/3 bg-outline/20 rounded"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Testimonials */}
      <section className="py-24 bg-surface-low">
        <div className="max-w-7xl mx-auto px-6">
          <h2 className="font-serif text-4xl font-bold text-center mb-16">Reencuentros que Conmueven</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {[
              { img: IMAGES.test1, name: "Elena Rodriguez", pet: "Humana de Max", text: "Estábamos devastados cuando Max desapareció. Gracias a las alertas de geolocalización, un vecino lo vio en 2 horas. ¡Ya está de vuelta en su alfombra favorita!" },
              { img: IMAGES.test2, name: "Julián Thorne", pet: "Humano de Luna", text: "El Pasaporte Digital para Mascotas hizo que fuera muy fácil compartir su foto en redes sociales de inmediato. La respuesta de la comunidad fue increíble." },
              { img: IMAGES.test3, name: "Sara Martinez", pet: "Humana de Oliver", text: "El algoritmo de coincidencia encontró una publicación en una plataforma diferente que yo no había visto. Es como tener un equipo de búsqueda dedicado las 24 horas, los 7 días de la semana." }
            ].map((t, i) => (
              <div key={i} className="bg-surface p-8 rounded-2xl shadow-sm flex flex-col gap-4">
                <div className="flex gap-1 text-tertiary">
                  {[...Array(5)].map((_, j) => <Star key={j} size={18} fill="currentColor" />)}
                </div>
                <p className="italic text-text-variant leading-relaxed flex-1">"{t.text}"</p>
                <div className="flex items-center gap-4 mt-4">
                  <img src={t.img} alt={t.name} className="w-12 h-12 rounded-full object-cover" referrerPolicy="no-referrer" />
                  <div>
                    <p className="font-bold text-text">{t.name}</p>
                    <p className="text-sm text-text-muted">{t.pet}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-24 px-6 bg-background">
        <div className="max-w-5xl mx-auto bg-primary rounded-[2rem] p-12 text-center text-surface relative overflow-hidden shadow-xl">
          <div className="absolute inset-0 opacity-10 pointer-events-none bg-[radial-gradient(circle_at_top_right,_var(--tw-gradient-stops))] from-white via-transparent to-transparent"></div>
          <div className="relative z-10 space-y-8">
            <h2 className="font-serif text-4xl md:text-5xl font-extrabold">Cada Segundo Cuenta</h2>
            <p className="text-xl opacity-90 max-w-2xl mx-auto">
              No esperes a una crisis. Registra a tu mascota hoy o contribuye a nuestra red de rastreo comunitaria para ayudar a reunir familias.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center pt-4">
              <button className="bg-surface text-primary px-10 py-4 rounded-xl font-bold text-lg hover:bg-surface-low transition-all">
                Únete a la Comunidad
              </button>
              <button className="bg-transparent border-2 border-surface text-surface px-10 py-4 rounded-xl font-bold text-lg hover:bg-surface/10 transition-all">
                Donar a la Causa
              </button>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};
