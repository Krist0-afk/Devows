import React from 'react';
import { Bell, User } from 'lucide-react';
import { NotificationMenu } from './NotificationMenu';

export const Navbar = ({ currentView, navigate, isLoggedIn, setIsLoggedIn }: { currentView: string, navigate: (v: string) => void, isLoggedIn: boolean, setIsLoggedIn: (v: boolean) => void }) => {
  return (
    <header className="fixed top-0 w-full z-[2000] flex justify-between items-center px-6 md:px-8 h-16 md:h-20 bg-background border-b border-outline/20 shadow-sm">
      <div 
        className="flex items-center gap-2 cursor-pointer"
        onClick={() => navigate('home')}
      >
        <span className="text-xl md:text-2xl font-serif text-primary font-bold">Sanos y Salvos</span>
      </div>
      
      <nav className="hidden md:flex items-center gap-8 ml-auto mr-10">
        <button onClick={() => navigate('home')} className={`text-sm font-medium tracking-tight transition-colors ${currentView === 'home' ? 'text-primary border-b-2 border-primary pb-1' : 'text-text-muted hover:text-primary'}`}>Inicio</button>
        <button onClick={() => navigate('lost-pets')} className={`text-sm font-medium tracking-tight transition-colors ${currentView === 'lost-pets' ? 'text-primary border-b-2 border-primary pb-1' : 'text-text-muted hover:text-primary'}`}>Mascotas Perdidas</button>
        <button onClick={() => navigate('report')} className={`text-sm font-medium tracking-tight transition-colors ${currentView === 'report' ? 'text-primary border-b-2 border-primary pb-1' : 'text-text-muted hover:text-primary'}`}>Reportar Mascota</button>
        <button onClick={() => navigate('success-stories')} className={`text-sm font-medium tracking-tight transition-colors ${currentView === 'success-stories' ? 'text-primary border-b-2 border-primary pb-1' : 'text-text-muted hover:text-primary'}`}>Historias de Éxito</button>
        <button onClick={() => navigate('map')} className={`text-sm font-medium tracking-tight transition-colors ${currentView === 'map' ? 'text-primary border-b-2 border-primary pb-1' : 'text-text-muted hover:text-primary'}`}>Mapa</button>
      </nav>

      <div className="flex items-center gap-2 md:gap-4">
        {isLoggedIn ? (
          <>
            <NotificationMenu />
            <button onClick={() => navigate('profile')} className="p-2 rounded-full hover:bg-surface-high transition-colors text-text-muted hidden md:block">
              <User size={20} />
            </button>
            <button onClick={() => { setIsLoggedIn(false); navigate('home'); }} className="text-sm font-medium text-text-muted hover:text-error transition-colors hidden md:block ml-2">
              Cerrar Sesión
            </button>
          </>
        ) : (
          <>
            <button onClick={() => navigate('login')} className="border border-primary text-primary px-4 md:px-6 py-2 rounded-lg font-medium text-sm hover:bg-primary/10 transition-colors hidden md:block">
              Iniciar Sesión
            </button>
            <button onClick={() => navigate('register')} className="bg-primary text-surface px-4 md:px-6 py-2 rounded-lg font-medium text-sm hover:opacity-90 transition-opacity">
              Registrarse
            </button>
          </>
        )}
      </div>
    </header>
  );
};
