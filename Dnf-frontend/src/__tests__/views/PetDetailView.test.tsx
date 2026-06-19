import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { PetDetailView } from '../../views/PetDetailView';

describe('PetDetailView Component', () => {
  const mockNavigate = vi.fn();

  it('renders pet details correctly', () => {
    render(<PetDetailView navigate={mockNavigate} />);
    
    expect(screen.getByText('Max')).toBeInTheDocument();
    expect(screen.getByText('Detalles de la Mascota')).toBeInTheDocument();
    expect(screen.getByText('Beagle')).toBeInTheDocument();
    expect(screen.getByText('Foro de Actualizaciones')).toBeInTheDocument();
  });

  it('handles navigate back to list', () => {
    render(<PetDetailView navigate={mockNavigate} />);
    
    fireEvent.click(screen.getByText('Volver a la lista'));
    expect(mockNavigate).toHaveBeenCalledWith('lost-pets');
  });
});
