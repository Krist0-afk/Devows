import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { SuccessStoriesView } from '../../views/SuccessStoriesView';
import { IMAGES } from '../../constants';

describe('SuccessStoriesView Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    global.fetch = vi.fn();
  });

  it('renders loading state initially', () => {
    (global.fetch as any).mockImplementation(() => new Promise(() => {})); // Never resolves
    render(<SuccessStoriesView />);
    
    expect(screen.getByText('Historias de Éxito')).toBeInTheDocument();
    expect(screen.getByText('Cargando historias felices...')).toBeInTheDocument();
  });

  it('fetches and displays success stories', async () => {
    const mockStories = [
      {
        id: 1,
        title: "Luna volvió a casa",
        timeAgo: "Hace 1 semana",
        content: "Test content",
        family: "Familia Martinez",
        location: "Zona Sur",
        image: IMAGES.petLuna
      }
    ];

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockStories,
    });

    render(<SuccessStoriesView />);

    await waitFor(() => {
      expect(screen.getByText('Luna volvió a casa')).toBeInTheDocument();
      expect(screen.queryByText('Cargando historias felices...')).not.toBeInTheDocument();
    });
  });

  it('handles fetch error gracefully', async () => {
    (global.fetch as any).mockRejectedValueOnce(new Error('Network error'));

    render(<SuccessStoriesView />);

    await waitFor(() => {
      expect(screen.queryByText('Cargando historias felices...')).not.toBeInTheDocument();
      // It won't crash and will just render an empty list of stories
    });
  });
});
