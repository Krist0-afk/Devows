import React from 'react';

export const Footer = () => (
  <footer className="bg-background border-t border-outline/20 mt-auto">
    <div className="flex flex-col md:flex-row justify-between items-center w-full px-8 py-12 gap-6 max-w-7xl mx-auto">
      <div className="space-y-4 text-center md:text-left">
        <div className="font-serif text-xl font-bold text-primary">Sanos y Salvos</div>
        <p className="text-sm text-text-muted">© 2026 Sanos y Salvos. Arraigados en la comunidad.</p>
      </div>
      <div className="flex flex-wrap justify-center gap-6 md:gap-8">
        <a className="text-sm text-text-muted hover:text-primary transition-colors" href="#">Política de Privacidad</a>
        <a className="text-sm text-text-muted hover:text-primary transition-colors" href="#">Guías Comunitarias</a>
        <a className="text-sm text-text-muted hover:text-primary transition-colors" href="#">Voluntariado</a>
        <a className="text-sm text-text-muted hover:text-primary transition-colors" href="#">Contacto</a>
      </div>
    </div>
  </footer>
);
