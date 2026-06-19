import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { LostPetsView } from '../../views/LostPetsView';
import { MOCK_PETS_DATA } from '../../constants';

describe('LostPetsView Component', () => {
  const mockNavigate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    global.fetch = vi.fn();
  });

  it('renders title and search bar', () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });
    
    render(<LostPetsView navigate={mockNavigate} />);
    expect(screen.getByText('Mascotas Perdidas')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Buscar por nombre, raza...')).toBeInTheDocument();
  });

  it('fetches and displays pets', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => MOCK_PETS_DATA.filter(p => p.status === 'lost'),
    });

    render(<LostPetsView navigate={mockNavigate} />);

    await waitFor(() => {
      expect(screen.getByText('Max')).toBeInTheDocument();
      expect(screen.getByText('Rocky')).toBeInTheDocument();
    });
  });

  it('handles navigation to map', () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(<LostPetsView navigate={mockNavigate} />);
    
    fireEvent.click(screen.getByText('Ver en Mapa'));
    expect(mockNavigate).toHaveBeenCalledWith('map');
  });

  it('fetches suggestions on search input', async () => {
    (global.fetch as any).mockResolvedValue({
      ok: true,
      json: async () => [MOCK_PETS_DATA[0]],
    });

    render(<LostPetsView navigate={mockNavigate} />);

    const searchInput = screen.getByPlaceholderText('Buscar por nombre, raza...');
    fireEvent.change(searchInput, { target: { value: 'Max' } });

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith('/api/pets/suggestions?q=Max', expect.any(Object));
    });
  });
});
