import { cleanup, render, screen } from '@testing-library/react';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { api } from './api/client.js';
import { App } from './App.jsx';

describe('App', () => {
  it('renders the login workflow when no session exists', () => {
    localStorage.clear();
    render(<App />);
    expect(screen.getByRole('heading', { name: /sign in/i })).toBeInTheDocument();
    expect(screen.getByDisplayValue('admin@payment.com')).toBeInTheDocument();
  });

  it('renders dashboard navigation when a user session exists', async () => {
    vi.spyOn(api, 'get').mockRejectedValue(new Error('offline'));
    localStorage.setItem('payment_console_user', JSON.stringify({ email: 'admin@payment.com', roles: ['ADMIN'] }));
    render(<App />);
    expect(screen.getByText('PayRecon')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /dashboard/i })).toBeInTheDocument();
    expect(await screen.findByText(/sample data/i)).toBeInTheDocument();
  });

  it('uses the configured API base URL', async () => {
    const client = await import('./api/client.js');
    expect(client.api.defaults.baseURL).toBeTruthy();
  });
});

beforeEach(() => {
  window.history.pushState({}, '', '/');
});

afterEach(() => {
  cleanup();
  localStorage.clear();
  vi.restoreAllMocks();
});
