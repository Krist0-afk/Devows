import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { AuthView } from '../../views/AuthView';

describe('AuthView Component', () => {
  const mockNavigate = vi.fn();
  const mockSetIsLoggedIn = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    global.fetch = vi.fn();
  });

  it('renders login form correctly', () => {
    render(<AuthView type="login" navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    expect(screen.getByText('¡Bienvenido de vuelta!')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('tu@correo.com')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('••••••••')).toBeInTheDocument();
  });

  it('renders register form correctly', () => {
    render(<AuthView type="register" navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    expect(screen.getByText('Únete a la comunidad')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Juan Pérez')).toBeInTheDocument();
  });

  it('handles successful login', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => ({ token: 'mock-token' }),
    });

    render(<AuthView type="login" navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    
    fireEvent.change(screen.getByPlaceholderText('tu@correo.com'), { target: { value: 'admin@dnf.cl' } });
    fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'Admin123!' } });
    
    fireEvent.click(screen.getByText('Iniciar Sesión'));

    await waitFor(() => {
      expect(mockSetIsLoggedIn).toHaveBeenCalledWith(true);
      expect(mockNavigate).toHaveBeenCalledWith('home');
    });
  });

  it('handles failed login', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: false,
      json: async () => ({ message: 'Credenciales inválidas' }),
    });

    render(<AuthView type="login" navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    
    fireEvent.change(screen.getByPlaceholderText('tu@correo.com'), { target: { value: 'wrong@test.com' } });
    fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'wrong' } });
    
    fireEvent.click(screen.getByText('Iniciar Sesión'));

    await waitFor(() => {
      expect(screen.getByText('Credenciales inválidas')).toBeInTheDocument();
      expect(mockSetIsLoggedIn).not.toHaveBeenCalled();
    });
  });

  it('handles registration', async () => {
    render(<AuthView type="register" navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    
    fireEvent.change(screen.getByPlaceholderText('Juan Pérez'), { target: { value: 'Test User' } });
    fireEvent.change(screen.getByPlaceholderText('tu@correo.com'), { target: { value: 'test@test.com' } });
    fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'Admin123!' } });
    
    fireEvent.click(screen.getByText('Crear Cuenta'));

    await waitFor(() => {
      expect(mockSetIsLoggedIn).toHaveBeenCalledWith(true);
      expect(mockNavigate).toHaveBeenCalledWith('home');
    });
  });

  it('toggles between login and register views', () => {
    render(<AuthView type="login" navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    
    fireEvent.click(screen.getByText('Regístrate aquí'));
    expect(mockNavigate).toHaveBeenCalledWith('register');
  });
});
