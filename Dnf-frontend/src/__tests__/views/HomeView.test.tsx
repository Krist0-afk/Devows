import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { HomeView } from '../../views/HomeView';

describe('HomeView Component', () => {
  it('renders the hero section title', () => {
    const navigate = vi.fn();
    render(<HomeView navigate={navigate} />);
    expect(screen.getByText(/Arraigados en la Comunidad/i)).toBeInTheDocument();
  });

  it('navigates to report and map correctly', () => {
    const navigate = vi.fn();
    render(<HomeView navigate={navigate} />);
    
    const reportBtn = screen.getByText('Reportar Mascota Perdida');
    fireEvent.click(reportBtn);
    expect(navigate).toHaveBeenCalledWith('report');

    const mapBtn = screen.getByText('Ver Mapa de Avistamientos');
    fireEvent.click(mapBtn);
    expect(navigate).toHaveBeenCalledWith('map');
  });

  it('renders testimonials section', () => {
    const navigate = vi.fn();
    render(<HomeView navigate={navigate} />);
    expect(screen.getByText('Reencuentros que Conmueven')).toBeInTheDocument();
    expect(screen.getByText('Elena Rodriguez')).toBeInTheDocument();
  });
});
