import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import App from '../App';

// Mock components to avoid heavy rendering
vi.mock('../components/Navbar', () => ({
  Navbar: ({ currentView, navigate }: any) => (
    <div data-testid="navbar">
      <button onClick={() => navigate('lost-pets')}>Go to Lost Pets</button>
      <span>Current: {currentView}</span>
    </div>
  ),
}));

vi.mock('../views/HomeView', () => ({
  HomeView: () => <div data-testid="home-view" />,
}));

vi.mock('../views/LostPetsView', () => ({
  LostPetsView: () => <div data-testid="lost-pets-view" />,
}));

describe('App Component', () => {
  it('renders navbar and initial home view', () => {
    render(<App />);
    expect(screen.getByTestId('navbar')).toBeInTheDocument();
    expect(screen.getByTestId('home-view')).toBeInTheDocument();
    expect(screen.getByText('Current: home')).toBeInTheDocument();
  });

  it('navigates to different views', async () => {
    render(<App />);
    
    const navBtn = screen.getByText('Go to Lost Pets');
    navBtn.click();
    
    await waitFor(() => {
      expect(screen.getByText('Current: lost-pets')).toBeInTheDocument();
      expect(screen.getByTestId('lost-pets-view')).toBeInTheDocument();
    });
  });
});
