import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { ReportPetView } from '../../views/ReportPetView';

// Mock react-leaflet components since they require DOM measurements
vi.mock('react-leaflet', () => ({
  MapContainer: ({ children }: any) => <div data-testid="map-container">{children}</div>,
  TileLayer: () => <div data-testid="tile-layer" />,
  Marker: () => <div data-testid="marker" />,
  useMapEvents: vi.fn(),
}));

describe('ReportPetView Component', () => {
  const mockNavigate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    global.fetch = vi.fn();
    window.alert = vi.fn();
  });

  it('renders login prompt when not logged in', () => {
    render(<ReportPetView isLoggedIn={false} navigate={mockNavigate} />);
    expect(screen.getByText('Inicia sesión para reportar')).toBeInTheDocument();
  });

  it('navigates to login when clicking Iniciar Sesión in prompt', () => {
    render(<ReportPetView isLoggedIn={false} navigate={mockNavigate} />);
    fireEvent.click(screen.getByText('Iniciar Sesión'));
    expect(mockNavigate).toHaveBeenCalledWith('login');
  });

  it('renders report form when logged in', () => {
    render(<ReportPetView isLoggedIn={true} navigate={mockNavigate} />);
    expect(screen.getByText('Reportar Mascota Perdida')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Ej. Max')).toBeInTheDocument();
  });

  it('submits form successfully', async () => {
    (global.fetch as any).mockResolvedValueOnce({ ok: true });

    render(<ReportPetView isLoggedIn={true} navigate={mockNavigate} />);
    
    fireEvent.change(screen.getByPlaceholderText('Ej. Max'), { target: { value: 'Buddy' } });
    fireEvent.change(screen.getByPlaceholderText('Ej. Beagle'), { target: { value: 'Golden' } });
    fireEvent.change(screen.getByPlaceholderText('Ej. Blanco con manchas marrones'), { target: { value: 'Dorado' } });
    fireEvent.change(screen.getByPlaceholderText('Ej. Parque Central, cerca de la fuente'), { target: { value: 'Plaza' } });
    // Get the date input by its type since there is no placeholder
    const dateInput = screen.getAllByRole('textbox').find(el => (el as HTMLInputElement).type === 'date') 
                   || document.querySelector('input[type="date"]');
    if (dateInput) {
      fireEvent.change(dateInput, { target: { value: '2023-10-10' } });
    }
    
    const form = screen.getByText('Publicar Reporte').closest('form')!;
    fireEvent.submit(form);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalled();
      expect(mockNavigate).toHaveBeenCalledWith('profile');
    });
  });

  it('handles form submission error', async () => {
    (global.fetch as any).mockResolvedValueOnce({ ok: false });

    render(<ReportPetView isLoggedIn={true} navigate={mockNavigate} />);
    
    const form = screen.getByText('Publicar Reporte').closest('form')!;
    fireEvent.submit(form);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith('Error al publicar el reporte.');
    });
  });
});
