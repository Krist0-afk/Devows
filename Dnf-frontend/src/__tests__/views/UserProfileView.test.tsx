import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { UserProfileView } from '../../views/UserProfileView';

describe('UserProfileView Component', () => {
  const mockNavigate = vi.fn();
  const mockSetIsLoggedIn = vi.fn();

  it('renders user details', () => {
    render(<UserProfileView navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    expect(screen.getByText('Juan Pérez')).toBeInTheDocument();
    expect(screen.getByText('juan.perez@ejemplo.com')).toBeInTheDocument();
  });

  it('renders tabs correctly and switches between them', () => {
    render(<UserProfileView navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    
    // Default tab is 'reports'
    expect(screen.getByText('Mis Reportes Activos')).toBeInTheDocument();

    // Click 'found' tab
    fireEvent.click(screen.getByText('Encontradas'));
    expect(screen.getByText('Mascotas Encontradas')).toBeInTheDocument();

    // Click 'settings' tab
    fireEvent.click(screen.getByText('Configuración'));
    expect(screen.getByText('Configuración de la Cuenta')).toBeInTheDocument();
  });

  it('navigates to report creation', () => {
    render(<UserProfileView navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    
    fireEvent.click(screen.getByText('+ Nuevo Reporte'));
    expect(mockNavigate).toHaveBeenCalledWith('report');
  });

  it('handles logout', () => {
    render(<UserProfileView navigate={mockNavigate} setIsLoggedIn={mockSetIsLoggedIn} />);
    
    fireEvent.click(screen.getByText('Cerrar Sesión'));
    expect(mockSetIsLoggedIn).toHaveBeenCalledWith(false);
    expect(mockNavigate).toHaveBeenCalledWith('home');
  });
});
