import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { NotificationMenu } from '../../components/NotificationMenu';

describe('NotificationMenu Component', () => {
  beforeEach(() => {
    global.fetch = vi.fn();
  });

  it('renders notification bell icon', () => {
    render(<NotificationMenu />);
    const button = screen.getByRole('button');
    expect(button).toBeInTheDocument();
  });

  it('opens and closes dropdown on click', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => [
        {
          id: 1,
          title: "Posible avistamiento de Max",
          message: "Alguien comentó en tu reporte de mascota perdida.",
          timeAgo: "Hace 10 min",
          read: false,
          type: "comment",
          petId: 1
        }
      ]
    });

    render(<NotificationMenu />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    expect(screen.getByText('Notificaciones')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText('Posible avistamiento de Max')).toBeInTheDocument();
    });

    // Close it
    fireEvent.click(button);
    await waitFor(() => {
      expect(screen.queryByText('Notificaciones')).not.toBeInTheDocument();
    });
  });
});
