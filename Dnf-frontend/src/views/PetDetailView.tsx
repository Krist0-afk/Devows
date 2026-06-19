import React from 'react';
import { fetchWithAuth } from '../utils/api';
import { MapPin, Share2, MessageCircle, ArrowLeft, Clock, AlertCircle } from 'lucide-react';
import { IMAGES } from '../constants';

export const PetDetailView = ({ navigate, petId }: { navigate: (v: string) => void, petId?: number }) => {
  const [pet, setPet] = React.useState<any>(null);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const loadPet = async () => {
      if (!petId) {
        setLoading(false);
        return;
      }
      try {
        const response = await fetchWithAuth(`/api/pets/${petId}`);
        if (response.ok) {
          const data = await response.json();
          setPet(data);
        }
      } catch (err) {
        console.error("Error loading pet details:", err);
      } finally {
        setLoading(false);
      }
    };
    loadPet();
  }, [petId]);

  if (loading) {
    return <div className="flex-1 bg-surface-low py-8 px-6 flex justify-center items-center">Cargando...</div>;
  }

  // Fallback a los datos antiguos (Max) si no hay petId o no se encontró
  const displayPet = pet || {
    name: "Max",
    type: "Perro",
    breed: "Beagle",
    gender: "Macho",
    color: "Tricolor",
    description: "Max es muy amigable pero puede estar asustado. Llevaba un collar azul con una placa en forma de hueso, pero sin número de teléfono actualizado. Tiene una pequeña cicatriz en la oreja izquierda.",
    status: "lost",
    timeAgo: "Hace 2 días",
    location: "Parque Central, cerca de la entrada principal. Visto por última vez corriendo hacia el norte.",
    image: IMAGES.petMax
  };

  return (
    <div className="flex-1 bg-surface-low py-8 px-6">
      <div className="max-w-6xl mx-auto">
        <button onClick={() => navigate('lost-pets')} className="flex items-center gap-2 text-text-muted hover:text-primary transition-colors mb-6 font-medium">
          <ArrowLeft size={20} /> Volver a la lista
        </button>

        <div className="bg-surface rounded-3xl overflow-hidden shadow-sm border border-outline/10">
          {/* Header/Banner */}
          <div className="relative h-64 md:h-96 bg-surface-high">
            <img src={displayPet.image || IMAGES.petMax} alt={displayPet.name} className="w-full h-full object-cover" referrerPolicy="no-referrer" />
            <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent"></div>
            <div className="absolute bottom-6 left-6 md:bottom-10 md:left-10 text-surface">
              <div className="flex items-center gap-3 mb-2">
                <span className={displayPet.status === 'lost' ? "bg-error px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider" : "bg-primary px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider"}>{displayPet.status === 'lost' ? 'Perdido' : 'Encontrado'}</span>
                <span className="bg-surface/20 backdrop-blur-md px-3 py-1 rounded-full text-xs font-medium flex items-center gap-1">
                  <Clock size={14} /> {displayPet.timeAgo || 'Recientemente'}
                </span>
              </div>
              <h1 className="text-4xl md:text-5xl font-serif font-bold">{displayPet.name}</h1>
            </div>
            <div className="absolute top-6 right-6 flex gap-3">
              <button className="w-10 h-10 rounded-full bg-surface/20 backdrop-blur-md flex items-center justify-center text-surface hover:bg-surface/40 transition-colors">
                <Share2 size={20} />
              </button>
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-0 lg:gap-8">
            {/* Left Column: Details & Forum */}
            <div className="lg:col-span-2 p-6 md:p-10 space-y-10">
              <section>
                <h2 className="text-2xl font-serif font-bold text-text mb-6">Detalles de la Mascota</h2>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 md:gap-6">
                  <div className="bg-surface-low p-4 rounded-2xl">
                    <p className="text-xs text-text-muted uppercase tracking-wider font-bold mb-1">Tipo</p>
                    <p className="font-medium text-text">{displayPet.type}</p>
                  </div>
                  <div className="bg-surface-low p-4 rounded-2xl">
                    <p className="text-xs text-text-muted uppercase tracking-wider font-bold mb-1">Raza</p>
                    <p className="font-medium text-text">{displayPet.breed || 'Mestizo'}</p>
                  </div>
                  <div className="bg-surface-low p-4 rounded-2xl">
                    <p className="text-xs text-text-muted uppercase tracking-wider font-bold mb-1">Sexo</p>
                    <p className="font-medium text-text">{displayPet.gender || 'Desconocido'}</p>
                  </div>
                  <div className="bg-surface-low p-4 rounded-2xl">
                    <p className="text-xs text-text-muted uppercase tracking-wider font-bold mb-1">Color</p>
                    <p className="font-medium text-text">{displayPet.color || 'No especificado'}</p>
                  </div>
                </div>
                
                <div className="mt-8 space-y-4">
                  <h3 className="font-bold text-text">Descripción Adicional</h3>
                  <p className="text-text-variant leading-relaxed">
                    {displayPet.description || 'Sin descripción adicional.'}
                  </p>
                </div>
              </section>

              <hr className="border-outline/10" />

              {/* Forum Section */}
              <section>
                <div className="flex items-center justify-between mb-6">
                  <h2 className="text-2xl font-serif font-bold text-text flex items-center gap-2">
                    <MessageCircle className="text-primary" /> Foro de Actualizaciones
                  </h2>
                  <span className="text-sm font-medium text-text-muted bg-surface-low px-3 py-1 rounded-full">3 comentarios</span>
                </div>
                
                <div className="space-y-6">
                  {/* Comment 1 */}
                  <div className="flex gap-4">
                    <div className="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center text-primary font-bold shrink-0">
                      LM
                    </div>
                    <div className="flex-1 bg-surface-low p-4 rounded-2xl rounded-tl-none">
                      <div className="flex justify-between items-start mb-2">
                        <p className="font-bold text-sm text-text">Laura M.</p>
                        <p className="text-xs text-text-muted">Hace 5 horas</p>
                      </div>
                      <p className="text-sm text-text-variant">Creo haberlo visto cerca del supermercado en la calle principal, pero salió corriendo cuando intenté acercarme.</p>
                    </div>
                  </div>
                  
                  {/* Comment 2 (Owner) */}
                  <div className="flex gap-4">
                    <div className="w-10 h-10 rounded-full bg-tertiary/10 flex items-center justify-center text-tertiary font-bold shrink-0">
                      JP
                    </div>
                    <div className="flex-1 bg-tertiary/5 border border-tertiary/20 p-4 rounded-2xl rounded-tl-none">
                      <div className="flex justify-between items-start mb-2">
                        <div className="flex items-center gap-2">
                          <p className="font-bold text-sm text-text">Juan P.</p>
                          <span className="text-[10px] font-bold uppercase tracking-wider bg-tertiary text-surface px-2 py-0.5 rounded-full">Dueño</span>
                        </div>
                        <p className="text-xs text-text-muted">Hace 4 horas</p>
                      </div>
                      <p className="text-sm text-text-variant">¡Gracias Laura! Voy para allá ahora mismo a buscar por la zona.</p>
                    </div>
                  </div>

                  {/* Comment 3 with image */}
                  <div className="flex gap-4">
                    <div className="w-10 h-10 rounded-full bg-secondary/10 flex items-center justify-center text-secondary font-bold shrink-0">
                      CR
                    </div>
                    <div className="flex-1 bg-surface-low p-4 rounded-2xl rounded-tl-none">
                      <div className="flex justify-between items-start mb-2">
                        <p className="font-bold text-sm text-text">Carlos R.</p>
                        <p className="text-xs text-text-muted">Hace 1 hora</p>
                      </div>
                      <p className="text-sm text-text-variant mb-3">¿Es este perrito? Estaba rondando por el parque hace un rato.</p>
                      <img src={IMAGES.forumPhoto} alt="Posible avistamiento" className="rounded-xl w-full max-w-sm object-cover h-48 border border-outline/10" referrerPolicy="no-referrer" />
                    </div>
                  </div>
                </div>

                {/* Add comment form */}
                <div className="mt-8 flex gap-4">
                  <div className="w-10 h-10 rounded-full bg-surface-high flex items-center justify-center text-text-muted shrink-0">
                    Tú
                  </div>
                  <div className="flex-1 relative">
                    <textarea 
                      rows={2} 
                      placeholder="Escribe una actualización o posible avistamiento..." 
                      className="w-full px-4 py-3 bg-background border border-outline/30 rounded-xl focus:ring-2 focus:ring-primary/50 outline-none transition-shadow resize-none text-sm"
                    ></textarea>
                    <div className="absolute bottom-3 right-3">
                      <button className="bg-primary text-surface px-4 py-1.5 rounded-lg text-sm font-bold hover:opacity-90 transition-opacity">
                        Publicar
                      </button>
                    </div>
                  </div>
                </div>
              </section>
            </div>

            {/* Right Column: Map & Actions */}
            <div className="bg-surface-low p-6 md:p-10 border-t lg:border-t-0 lg:border-l border-outline/10 space-y-8">
              <div className="space-y-4">
                <button className="w-full bg-primary text-surface py-4 rounded-xl font-bold text-lg hover:opacity-90 transition-opacity shadow-md shadow-primary/20">
                  Contactar Dueño
                </button>
                <button className="w-full bg-surface border-2 border-primary/20 text-primary py-4 rounded-xl font-bold text-lg hover:bg-primary/5 transition-colors">
                  Reportar Avistamiento
                </button>
              </div>

              <div className="space-y-4">
                <h3 className="font-bold text-text flex items-center gap-2">
                  <MapPin size={18} className="text-primary" /> Última Ubicación
                </h3>
                <p className="text-sm text-text-variant">{displayPet.location}</p>
                <div className="h-48 bg-surface-high rounded-2xl overflow-hidden border border-outline/20 relative">
                  <img src={IMAGES.miniMap} alt="Mapa" className="w-full h-full object-cover opacity-80" referrerPolicy="no-referrer" />
                  <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-4 h-4 bg-error rounded-full border-2 border-surface shadow-lg animate-pulse"></div>
                </div>
              </div>

              <div className="bg-tertiary/5 border border-tertiary/20 rounded-2xl p-5">
                <h4 className="font-bold text-tertiary flex items-center gap-2 mb-2">
                  <AlertCircle size={16} /> Consejos de búsqueda
                </h4>
                <ul className="text-sm text-text-variant space-y-2 list-disc list-inside">
                  <li>No persigas a la mascota si la ves.</li>
                  <li>Llama al dueño inmediatamente.</li>
                  <li>Intenta tomar una foto clara.</li>
                  <li>Anota la dirección en la que huyó.</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
