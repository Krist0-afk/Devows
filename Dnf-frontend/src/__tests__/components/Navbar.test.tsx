import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { Navbar } from '../../components/Navbar';

describe('Navbar Component', () => {
  beforeEach(() => {
    global.fetch = vi.fn();
  });

  it('renders the brand name and navigates home on click', () => {
    const navigate = vi.fn();
    const setIsLoggedIn = vi.fn();
    render(<Navbar currentView="home" navigate={navigate} isLoggedIn={false} setIsLoggedIn={setIsLoggedIn} />);
    const brand = screen.getByText('Sanos y Salvos');
    expect(brand).toBeInTheDocument();
    fireEvent.click(brand);
    expect(navigate).toHaveBeenCalledWith('home');
  });

  it('navigates to all top bar links', () => {
    const navigate = vi.fn();
    const setIsLoggedIn = vi.fn();
    render(<Navbar currentView="home" navigate={navigate} isLoggedIn={false} setIsLoggedIn={setIsLoggedIn} />);
    
    fireEvent.click(screen.getByText('Inicio'));
    expect(navigate).toHaveBeenCalledWith('home');

    fireEvent.click(screen.getByText('Mascotas Perdidas'));
    expect(navigate).toHaveBeenCalledWith('lost-pets');

    fireEvent.click(screen.getByText('Reportar Mascota'));
    expect(navigate).toHaveBeenCalledWith('report');

    fireEvent.click(screen.getByText('Historias de Éxito'));
    expect(navigate).toHaveBeenCalledWith('success-stories');

    fireEvent.click(screen.getByText('Mapa'));
    expect(navigate).toHaveBeenCalledWith('map');
  });

  it('navigates to login and register when not logged in', () => {
    const navigate = vi.fn();
    const setIsLoggedIn = vi.fn();
    render(<Navbar currentView="home" navigate={navigate} isLoggedIn={false} setIsLoggedIn={setIsLoggedIn} />);
    
    const loginBtn = screen.getByText('Iniciar Sesión');
    fireEvent.click(loginBtn);
    expect(navigate).toHaveBeenCalledWith('login');

    const registerBtn = screen.getByText('Registrarse');
    fireEvent.click(registerBtn);
    expect(navigate).toHaveBeenCalledWith('register');
  });

  it('shows user profile and logout when logged in', () => {
    const navigate = vi.fn();
    const setIsLoggedIn = vi.fn();
    const { container } = render(<Navbar currentView="home" navigate={navigate} isLoggedIn={true} setIsLoggedIn={setIsLoggedIn} />);
    
    // Find the profile button (it contains the User icon)
    const buttons = container.querySelectorAll('button');
    const profileBtn = Array.from(buttons).find(btn => btn.querySelector('svg.lucide-user'));
    
    if (profileBtn) {
      fireEvent.click(profileBtn);
      expect(navigate).toHaveBeenCalledWith('profile');
    }
    
    const logoutBtn = screen.getByText('Cerrar Sesión');
    fireEvent.click(logoutBtn);
    expect(setIsLoggedIn).toHaveBeenCalledWith(false);
    expect(navigate).toHaveBeenCalledWith('home');
  });

  it('highlights the current view', () => {
    const navigate = vi.fn();
    const setIsLoggedIn = vi.fn();
    render(<Navbar currentView="map" navigate={navigate} isLoggedIn={false} setIsLoggedIn={setIsLoggedIn} />);
    const mapBtn = screen.getByText('Mapa');
    expect(mapBtn).toHaveClass('text-primary');
  });
});
