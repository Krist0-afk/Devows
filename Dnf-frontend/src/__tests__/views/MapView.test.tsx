import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { MapView } from '../../views/MapView';
import { MOCK_PETS_DATA } from '../../constants';

// Mock react-leaflet components
vi.mock('react-leaflet', () => ({
  MapContainer: ({ children }: any) => <div data-testid="map-container">{children}</div>,
  TileLayer: () => <div data-testid="tile-layer" />,
  Marker: ({ children }: any) => <div data-testid="marker">{children}</div>,
  Popup: ({ children }: any) => <div data-testid="popup">{children}</div>,
  useMap: () => ({
    flyTo: vi.fn(),
    zoomIn: vi.fn(),
    zoomOut: vi.fn(),
    locate: vi.fn(),
  }),
}));

describe('MapView Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    global.fetch = vi.fn();
  });

  it('renders sidebar with filters', () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(<MapView />);
    expect(screen.getByText('Buscar Mascotas')).toBeInTheDocument();
    expect(screen.getByText('Estado del Caso')).toBeInTheDocument();
    expect(screen.getByText('Mascotas Perdidas')).toBeInTheDocument();
    expect(screen.getByText('Mascotas Encontradas')).toBeInTheDocument();
  });

  it('fetches pets on mount and displays results count', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => MOCK_PETS_DATA.map(pet => ({ ...pet, latitude: pet.coordinates[0], longitude: pet.coordinates[1] })),
    });

    render(<MapView />);

    await waitFor(() => {
      // 4 pets total in mock data
      expect(screen.getByText('4')).toBeInTheDocument();
    });
  });

  it('handles search form submission', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => MOCK_PETS_DATA.map(pet => ({ ...pet, latitude: pet.coordinates[0], longitude: pet.coordinates[1] })),
    });

    render(<MapView />);

    const input = screen.getByPlaceholderText('Busca mascota o ubicación...');
    fireEvent.change(input, { target: { value: 'Max' } });

    const goButton = screen.getByText('IR');
    fireEvent.click(goButton);

    await waitFor(() => {
      expect(screen.getByText('1')).toBeInTheDocument(); // Only Max matches
    });
  });

  it('toggles filters', async () => {
    (global.fetch as any).mockResolvedValue({
      ok: true,
      json: async () => MOCK_PETS_DATA.map(pet => ({ ...pet, latitude: pet.coordinates[0], longitude: pet.coordinates[1] })),
    });

    render(<MapView />);
    
    // Wait for initial load
    await waitFor(() => {
      expect(screen.getByText('4')).toBeInTheDocument();
    });

    // Uncheck lost
    const lostCheckbox = screen.getByRole('checkbox', { name: /Mascotas Perdidas/i });
    fireEvent.click(lostCheckbox);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith('/api/pets?status=found&type=', expect.anything());
    });
  });
});
