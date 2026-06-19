import React, { useState, useCallback, useEffect } from 'react';
import { fetchWithAuth } from '../utils/api';
import { MapContainer, TileLayer, Marker, Popup, Tooltip, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { Search, Plus, Minus, LocateFixed, Loader2 } from 'lucide-react';
import { MOCK_PETS_DATA } from '../constants';

const createIcon = (colorClass: string) => L.divIcon({
  className: 'custom-leaflet-icon',
  html: `<div class="w-10 h-10 ${colorClass} rounded-full border-4 border-white shadow-xl flex items-center justify-center text-white"><svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" x2="12" y1="8" y2="12"/><line x1="12" x2="12.01" y1="16" y2="16"/></svg></div>`,
  iconSize: [40, 40],
  iconAnchor: [20, 40],
  popupAnchor: [0, -45],
});

const lostIcon = createIcon('bg-error');
const foundIcon = createIcon('bg-primary');

// Component to control map view
const MapController = ({ center }: { center: [number, number] | null }) => {
  const map = useMap();
  React.useEffect(() => {
    if (center) {
      map.flyTo(center, 14, { duration: 1.5 });
    }
  }, [center, map]);
  return null;
};

const CustomZoomControls = () => {
  const map = useMap();
  return (
    <div className="absolute top-6 right-6 flex flex-col gap-2 z-[1000]">
      <button onClick={() => map.zoomIn()} className="w-10 h-10 bg-surface rounded-xl shadow-md flex items-center justify-center text-text hover:bg-surface-low transition-colors cursor-pointer">
        <Plus size={20} />
      </button>
      <button onClick={() => map.zoomOut()} className="w-10 h-10 bg-surface rounded-xl shadow-md flex items-center justify-center text-text hover:bg-surface-low transition-colors cursor-pointer">
        <Minus size={20} />
      </button>
      <button onClick={() => map.locate({setView: true, maxZoom: 16})} className="w-10 h-10 bg-surface rounded-xl shadow-md flex items-center justify-center text-text hover:bg-surface-low transition-colors mt-4 cursor-pointer">
        <LocateFixed size={20} />
      </button>
    </div>
  );
};

export const MapView = ({ navigate }: { navigate: (v: string, data?: any) => void }) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [isSearching, setIsSearching] = useState(false);
  const [mapCenter, setMapCenter] = useState<[number, number] | null>([-41.4693, -72.9424]);
  const [pets, setPets] = useState<any[]>([]);
  const [suggestions, setSuggestions] = useState<any[]>([]);
  const [filterType, setFilterType] = useState('all');
  const [showLost, setShowLost] = useState(true);
  const [showFound, setShowFound] = useState(true);

  const fetchPets = useCallback(async () => {
    try {
      const response = await fetchWithAuth(`/api/pets?status=${showLost && !showFound ? 'lost' : !showLost && showFound ? 'found' : ''}&type=${filterType === 'all' ? '' : filterType}`);
      if (response.ok) {
        const data = await response.json();
        // Mapear los datos de la API para asegurar que tienen la propiedad coordinates esperada por Leaflet
        const mappedData = data.map((pet: any) => ({
          ...pet,
          coordinates: pet.latitude != null && pet.longitude != null 
            ? [pet.latitude, pet.longitude] 
            : null
        })).filter((pet: any) => pet.coordinates != null); // Omitir mascotas sin coordenadas válidas
        setPets(mappedData);
      } else {
        setPets(MOCK_PETS_DATA);
      }
    } catch (err) {
      console.error('Error fetching pets:', err);
      setPets(MOCK_PETS_DATA);
    }
  }, [showLost, showFound, filterType]);

  useEffect(() => {
    fetchPets();
  }, [fetchPets]);

  useEffect(() => {
    const fetchSuggestions = async () => {
      if (searchQuery.length < 1) {
        setSuggestions([]);
        return;
      }
      try {
        const response = await fetchWithAuth(`/api/pets/suggestions?q=${searchQuery}`);
        if (response.ok) {
          const data = await response.json();
          setSuggestions(data);
        }
      } catch (err) {
        console.error('Error fetching suggestions:', err);
      }
    };

    const timer = setTimeout(fetchSuggestions, 200);
    return () => clearTimeout(timer);
  }, [searchQuery]);

  const filteredPets = pets.filter(pet => {
    const matchesSearch = 
      (pet.name && pet.name.toLowerCase().includes(searchQuery.toLowerCase())) ||
      (pet.breed && pet.breed.toLowerCase().includes(searchQuery.toLowerCase()));
    
    return matchesSearch;
  });

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;

    // 1. Check if it's a pet name in currently loaded pets
    const foundPet = pets.find(p => p.name.toLowerCase() === searchQuery.toLowerCase());
    if (foundPet) {
      setMapCenter(foundPet.coordinates);
      return;
    }

    // 2. Otherwise search for location using Nominatim
    setIsSearching(true);
    try {
      const response = await fetchWithAuth(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(searchQuery)}&limit=1`);
      const data = await response.json();
      if (data && data.length > 0) {
        setMapCenter([parseFloat(data[0].lat), parseFloat(data[0].lon)]);
      }
    } catch (error) {
      console.error("Search error:", error);
    } finally {
      setIsSearching(false);
    }
  };

  return (
    <div className="flex-1 flex overflow-hidden">
      {/* Sidebar */}
      <aside className="w-80 bg-surface border-r border-outline/20 flex flex-col z-[1000] overflow-y-auto shrink-0 shadow-xl">
        <div className="p-6 space-y-8">
          <div>
            <h2 className="text-2xl font-serif text-text mb-2">Buscar Mascotas</h2>
            <p className="text-sm text-text-variant leading-relaxed">Filtra el mapa para encontrar casos activos en tu área.</p>
          </div>

          {/* Status */}
          <div className="space-y-3">
            <label className="text-xs font-bold uppercase tracking-wider text-text-muted">Estado del Caso</label>
            <div className="flex flex-col gap-2">
              <label className="flex items-center gap-3 p-3 bg-surface-low rounded-xl cursor-pointer hover:bg-surface-container transition-colors border border-outline/10">
                <input 
                  type="checkbox" 
                  checked={showLost}
                  onChange={(e) => setShowLost(e.target.checked)}
                  className="w-5 h-5 rounded border-outline text-error focus:ring-error accent-error" 
                />
                <span className="flex items-center gap-2 text-sm font-medium text-text">
                  <span className="w-2 h-2 rounded-full bg-error"></span> Mascotas Perdidas
                </span>
              </label>
              <label className="flex items-center gap-3 p-3 bg-surface-low rounded-xl cursor-pointer hover:bg-surface-container transition-colors border border-outline/10">
                <input 
                  type="checkbox" 
                  checked={showFound}
                  onChange={(e) => setShowFound(e.target.checked)}
                  className="w-5 h-5 rounded border-outline text-primary focus:ring-primary accent-primary" 
                />
                <span className="flex items-center gap-2 text-sm font-medium text-text">
                  <span className="w-2 h-2 rounded-full bg-primary"></span> Mascotas Encontradas
                </span>
              </label>
            </div>
          </div>

          {/* Breed */}
          <div className="space-y-3">
            <label className="text-xs font-bold uppercase tracking-wider text-text-muted">Tipo de Mascota</label>
            <select 
              value={filterType}
              onChange={(e) => setFilterType(e.target.value)}
              className="w-full bg-surface-low border border-outline/20 rounded-xl p-3 text-sm focus:outline-none focus:ring-2 focus:ring-primary/50 text-text"
            >
              <option value="all">Todos los tipos</option>
              <option value="perro">Perro</option>
              <option value="gato">Gato</option>
            </select>
          </div>

          {/* Timeframe */}
          <div className="space-y-3">
            <label className="text-xs font-bold uppercase tracking-wider text-text-muted">Período</label>
            <div className="grid grid-cols-2 gap-2">
              <button className="p-2 border border-outline/20 rounded-lg text-xs font-medium hover:bg-secondary-container transition-colors text-text">Últimas 24h</button>
              <button className="p-2 border border-outline/20 rounded-lg text-xs font-medium hover:bg-secondary-container transition-colors text-text">Últimos 7d</button>
              <button className="p-2 border border-primary/30 rounded-lg text-xs font-medium bg-secondary-container text-primary">Últimos 30d</button>
              <button className="p-2 border border-outline/20 rounded-lg text-xs font-medium hover:bg-secondary-container transition-colors text-text">Personalizado</button>
            </div>
          </div>

          {/* Radius */}
          <div className="space-y-3">
            <div className="flex justify-between items-center">
              <label className="text-xs font-bold uppercase tracking-wider text-text-muted">Radio de Búsqueda</label>
              <span className="text-xs font-bold text-primary">5.0 km</span>
            </div>
            <input type="range" min="1" max="50" defaultValue="5" className="w-full h-2 bg-outline/30 rounded-lg appearance-none cursor-pointer accent-primary" />
          </div>
        </div>
      </aside>

      {/* Map Area */}
      <section className="flex-1 relative bg-surface-high">
        {/* Search Bar */}
        <div className="absolute top-6 left-1/2 -translate-x-1/2 w-full max-w-md px-4 z-[1000]">
          <div className="relative group">
            <form onSubmit={handleSearch} className="bg-surface rounded-2xl shadow-lg flex items-center p-1.5 border border-outline/10 overflow-hidden">
              <Search className="text-text-muted mx-3 shrink-0" size={20} />
              <input 
                type="text" 
                placeholder="Busca mascota o ubicación..." 
                value={searchQuery}
                onFocus={() => setShowSuggestions(true)}
                onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
                onChange={(e) => {
                  setSearchQuery(e.target.value);
                  setShowSuggestions(true);
                }}
                className="flex-1 border-none focus:outline-none text-sm py-2 bg-transparent text-text" 
              />
              <button 
                type="submit"
                disabled={isSearching}
                className="bg-secondary-container text-primary px-5 py-2.5 rounded-xl text-xs font-bold hover:bg-surface-high transition-colors flex items-center gap-2"
              >
                {isSearching ? <Loader2 className="animate-spin" size={14} /> : 'IR'}
              </button>
            </form>

            {showSuggestions && suggestions.length > 0 && (
              <div className="absolute top-full left-0 right-0 mt-2 bg-surface border border-outline/10 rounded-xl shadow-xl z-50 overflow-hidden">
                {suggestions.map((pet) => (
                  <button
                    key={pet.id}
                    onClick={() => {
                      setSearchQuery(pet.name);
                      setMapCenter(pet.coordinates);
                      setShowSuggestions(false);
                    }}
                    className="w-full text-left px-4 py-3 hover:bg-surface-low flex items-center gap-3 transition-colors border-b border-outline/5 last:border-0"
                  >
                    <img src={pet.image} alt={pet.name} className="w-8 h-8 rounded-full object-cover" referrerPolicy="no-referrer" />
                    <div>
                      <p className="text-sm font-bold text-text">{pet.name}</p>
                      <p className="text-[10px] text-text-variant">{pet.status === 'lost' ? 'Perdido' : 'Encontrado'} • {pet.breed}</p>
                    </div>
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>

        <MapContainer 
          center={mapCenter || [-41.4693, -72.9424]} 
          zoom={13} 
          zoomControl={false}
          className="w-full h-full z-0"
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          <CustomZoomControls />
          <MapController center={mapCenter} />
          
          {filteredPets.map(pet => (
            <Marker 
              key={pet.id} 
              position={pet.coordinates} 
              icon={pet.status === 'lost' ? lostIcon : foundIcon}
              eventHandlers={{
                click: () => navigate('detail', { id: pet.id })
              }}
            >
              <Tooltip direction="top" offset={[0, -40]} opacity={1} className="font-sans">
                <span className="font-bold text-sm text-text">{pet.name}</span> <span className="text-xs text-text-variant ml-1">({pet.breed})</span>
              </Tooltip>
              <Popup className="rounded-xl">
                <div className="font-bold text-text cursor-pointer hover:text-primary transition-colors" onClick={() => navigate('detail', { id: pet.id })}>{pet.name}</div>
                <div className="text-xs text-text-muted">{pet.status === 'lost' ? 'Perdido' : 'Encontrado'} • {pet.timeAgo}</div>
                <div className="text-[10px] text-text-variant mt-1 italic">{pet.location}</div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>

        {/* Stats */}
        <div className="absolute bottom-8 right-6 flex flex-col gap-3 z-[1000] pointer-events-none">
          <div className="bg-surface/90 backdrop-blur-md p-4 rounded-2xl shadow-lg border border-surface/50 min-w-[160px] pointer-events-auto">
            <div className="flex justify-between items-center mb-2">
              <span className="text-[10px] font-bold uppercase tracking-widest text-text-muted">Resultados</span>
              <span className="w-2 h-2 rounded-full bg-error animate-pulse"></span>
            </div>
            <div className="flex items-end gap-1">
              <span className="text-3xl font-serif font-bold text-text leading-none">{filteredPets.length}</span>
              <span className="text-xs text-text-variant mb-1">en esta área</span>
            </div>
          </div>
          <div className="bg-surface/90 backdrop-blur-md px-4 py-3 rounded-2xl shadow-lg border border-surface/50 flex items-center gap-4 pointer-events-auto">
            <div className="flex items-center gap-2">
              <span className="w-3 h-3 rounded-full bg-error"></span>
              <span className="text-xs font-bold text-text">Perdidos</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-3 h-3 rounded-full bg-primary"></span>
              <span className="text-xs font-bold text-text">Encontrados</span>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

