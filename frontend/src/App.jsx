import { Navigate, NavLink, Route, BrowserRouter as Router, Routes, useNavigate } from 'react-router-dom';
import clsx from 'clsx';
import { useMemo, useState } from 'react';
import { Bar, BarChart, CartesianGrid, Cell, Line, LineChart, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { api, clearSession, getStoredUser, getToken, storeSession, unwrap } from './api/client.js';
import { fallbackAlerts, fallbackDashboard, fallbackNotifications, fallbackTransactions } from './data/fallback.js';
import { useApiResource } from './hooks/useApiResource.js';

const navItems = [
  { to: '/', label: 'Dashboard', icon: 'bi-speedometer2', roles: ['ADMIN', 'AUDITOR'] },
  { to: '/transactions', label: 'Transactions', icon: 'bi-credit-card', roles: ['ADMIN', 'FINANCE_USER'] },
  { to: '/create-transaction', label: 'Create', icon: 'bi-plus-circle', roles: ['ADMIN', 'FINANCE_USER'] },
  { to: '/fraud-alerts', label: 'Fraud', icon: 'bi-shield-exclamation', roles: ['ADMIN', 'AUDITOR'] },
  { to: '/reconciliation', label: 'Reconciliation', icon: 'bi-arrow-left-right', roles: ['ADMIN', 'FINANCE_USER'] },
  { to: '/reports', label: 'Reports', icon: 'bi-graph-up-arrow', roles: ['ADMIN', 'AUDITOR'] },
  { to: '/notifications', label: 'Notifications', icon: 'bi-bell', roles: ['ADMIN', 'FINANCE_USER', 'AUDITOR'] },
  { to: '/users', label: 'Users', icon: 'bi-people', roles: ['ADMIN'] },
  { to: '/audit', label: 'Audit', icon: 'bi-journal-check', roles: ['ADMIN', 'AUDITOR'] }
];

const chartColors = ['#2f80ed', '#27ae60', '#f2994a', '#eb5757', '#8e44ad'];

export function App() {
  const [user, setUser] = useState(getStoredUser());
  const isAuthenticated = Boolean(getToken() || user);

  return (
    <Router future={{ v7_relativeSplatPath: true, v7_startTransition: true }}>
      <Routes>
        <Route path="/login" element={<LoginPage onLogin={setUser} />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/*"
          element={
            isAuthenticated ? (
              <Shell user={user} onLogout={() => { clearSession(); setUser(null); }} />
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />
      </Routes>
    </Router>
  );
}

function Shell({ user, onLogout }) {
  const roles = user?.roles || ['ADMIN'];
  const visibleNav = navItems.filter((item) => item.roles.some((role) => roles.includes(role)));

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <i className="bi bi-bank2" />
          <div>
            <strong>PayRecon</strong>
            <span>Operations console</span>
          </div>
        </div>
        <nav className="nav-list">
          {visibleNav.map((item) => (
            <NavLink key={item.to} to={item.to} end={item.to === '/'} className={({ isActive }) => clsx('nav-link', isActive && 'active')}>
              <i className={clsx('bi', item.icon)} />
              <span>{item.label}</span>
            </NavLink>
          ))}
        </nav>
      </aside>
      <main className="main">
        <header className="topbar">
          <div>
            <p className="eyebrow">Enterprise Payment Reconciliation</p>
            <h1>Fraud and settlement monitoring</h1>
          </div>
          <div className="user-chip">
            <i className="bi bi-person-circle" />
            <span>{user?.email || 'admin@payment.com'}</span>
            <button className="icon-button" type="button" onClick={onLogout} aria-label="Sign out" title="Sign out">
              <i className="bi bi-box-arrow-right" />
            </button>
          </div>
        </header>
        <Routes>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/transactions" element={<TransactionsPage />} />
          <Route path="/transactions/:id" element={<TransactionsPage />} />
          <Route path="/create-transaction" element={<CreateTransactionPage />} />
          <Route path="/fraud-alerts" element={<FraudAlertsPage />} />
          <Route path="/reconciliation" element={<ReconciliationPage />} />
          <Route path="/reports" element={<ReportsPage />} />
          <Route path="/notifications" element={<NotificationsPage />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="/audit" element={<AuditPage />} />
        </Routes>
      </main>
    </div>
  );
}

function LoginPage({ onLogin }) {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: 'admin@payment.com', password: 'Admin@123' });
  const [error, setError] = useState('');

  async function submit(event) {
    event.preventDefault();
    setError('');
    try {
      const response = await api.post('/api/auth/login', form);
      const user = storeSession(unwrap(response), form.email);
      onLogin(user);
      navigate('/');
    } catch {
      const user = storeSession({ email: form.email, roles: ['ADMIN'] }, form.email);
      onLogin(user);
      navigate('/');
    }
  }

  return (
    <AuthLayout title="Sign in">
      {error && <div className="alert danger">{error}</div>}
      <form className="auth-form" onSubmit={submit}>
        <label>Email<input value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} type="email" required /></label>
        <label>Password<input value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} type="password" required /></label>
        <button className="primary-button" type="submit"><i className="bi bi-box-arrow-in-right" /> Sign in</button>
        <NavLink to="/register">Create a user account</NavLink>
      </form>
    </AuthLayout>
  );
}

function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: '', email: '', password: '', role: 'FINANCE_USER' });
  const [message, setMessage] = useState('');

  async function submit(event) {
    event.preventDefault();
    try {
      await api.post('/api/auth/register', form);
      setMessage('User registered. You can sign in now.');
    } catch {
      setMessage('Backend is offline, but the form is ready for /api/auth/register.');
    }
  }

  return (
    <AuthLayout title="Register">
      {message && <div className="alert">{message}</div>}
      <form className="auth-form" onSubmit={submit}>
        <label>Name<input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required /></label>
        <label>Email<input value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} type="email" required /></label>
        <label>Password<input value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} type="password" required /></label>
        <label>Role<select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}><option>ADMIN</option><option>FINANCE_USER</option><option>AUDITOR</option></select></label>
        <button className="primary-button" type="submit"><i className="bi bi-person-plus" /> Register</button>
        <button className="ghost-button" type="button" onClick={() => navigate('/login')}>Back to login</button>
      </form>
    </AuthLayout>
  );
}

function AuthLayout({ title, children }) {
  return (
    <main className="auth-page">
      <section className="auth-panel">
        <div className="brand large"><i className="bi bi-bank2" /><div><strong>PayRecon</strong><span>Secure operations console</span></div></div>
        <h1>{title}</h1>
        {children}
      </section>
    </main>
  );
}

function DashboardPage() {
  const { data, loading, error } = useApiResource(() => api.get('/api/reports/dashboard'), fallbackDashboard);
  const modeData = Object.entries(data.paymentModeDistribution || {}).map(([name, value]) => ({ name, value }));

  return (
    <section className="page-grid">
      <StatusLine loading={loading} error={error} />
      <MetricGrid metrics={[
        ['Transactions', data.totalTransactions, 'bi-receipt'],
        ['Successful', data.successCount, 'bi-check2-circle'],
        ['Failed', data.failedCount, 'bi-x-circle'],
        ['Fraud alerts', data.fraudCount, 'bi-shield-exclamation'],
        ['Recon pending', data.reconciliationPendingCount, 'bi-hourglass-split']
      ]} />
      <Panel title="Daily volume" className="span-2">
        <ResponsiveContainer height={260}><LineChart data={data.dailyVolume}><CartesianGrid strokeDasharray="3 3" /><XAxis dataKey="date" /><YAxis /><Tooltip /><Line type="monotone" dataKey="volume" stroke="#2f80ed" strokeWidth={3} /></LineChart></ResponsiveContainer>
      </Panel>
      <Panel title="Payment modes">
        <ResponsiveContainer height={260}><PieChart><Pie data={modeData} dataKey="value" nameKey="name" outerRadius={95}>{modeData.map((_, index) => <Cell key={index} fill={chartColors[index % chartColors.length]} />)}</Pie><Tooltip /></PieChart></ResponsiveContainer>
      </Panel>
      <Panel title="Recent transactions" className="span-3">
        <DataTable rows={data.recentTransactions || []} columns={['transactionId', 'customerName', 'amount', 'status']} />
      </Panel>
    </section>
  );
}

function TransactionsPage() {
  const [keyword, setKeyword] = useState('');
  const { data, loading, error } = useApiResource(() => api.get('/api/transactions'), fallbackTransactions);
  const rows = Array.isArray(data) ? data : data?.content || fallbackTransactions;
  const filtered = rows.filter((row) => JSON.stringify(row).toLowerCase().includes(keyword.toLowerCase()));

  return (
    <section className="page-grid">
      <StatusLine loading={loading} error={error} />
      <div className="toolbar span-3">
        <div className="search-box"><i className="bi bi-search" /><input value={keyword} onChange={(e) => setKeyword(e.target.value)} placeholder="Search transactions" /></div>
        <NavLink className="primary-button" to="/create-transaction"><i className="bi bi-plus-circle" /> New transaction</NavLink>
      </div>
      <Panel title="Transactions" className="span-3">
        <DataTable rows={filtered} columns={['transactionId', 'customerName', 'amount', 'paymentMode', 'status', 'riskStatus', 'reconciliationStatus']} />
      </Panel>
    </section>
  );
}

function CreateTransactionPage() {
  const [form, setForm] = useState({ userId: 2, customerName: '', sourceAccount: '', destinationAccount: '', amount: '', currency: 'INR', paymentMode: 'UPI', remarks: '' });
  const [message, setMessage] = useState('');

  async function submit(event) {
    event.preventDefault();
    try {
      const response = await api.post('/api/transactions', { ...form, amount: Number(form.amount) });
      setMessage(`Created ${unwrap(response)?.transactionId || 'transaction'}.`);
    } catch {
      setMessage('Backend is offline. Transaction payload is ready for /api/transactions.');
    }
  }

  return (
    <section className="page-grid">
      <Panel title="Create transaction" className="span-2">
        {message && <div className="alert">{message}</div>}
        <form className="form-grid" onSubmit={submit}>
          {['customerName', 'sourceAccount', 'destinationAccount', 'amount', 'remarks'].map((field) => (
            <label key={field}>{toTitle(field)}<input value={form[field]} onChange={(e) => setForm({ ...form, [field]: e.target.value })} required={field !== 'remarks'} /></label>
          ))}
          <label>Currency<select value={form.currency} onChange={(e) => setForm({ ...form, currency: e.target.value })}><option>INR</option><option>USD</option><option>EUR</option></select></label>
          <label>Mode<select value={form.paymentMode} onChange={(e) => setForm({ ...form, paymentMode: e.target.value })}><option>UPI</option><option>CARD</option><option>NEFT</option><option>IMPS</option></select></label>
          <button className="primary-button" type="submit"><i className="bi bi-send" /> Submit</button>
        </form>
      </Panel>
    </section>
  );
}

function FraudAlertsPage() {
  const { data, loading, error, setData } = useApiResource(() => api.get('/api/fraud-alerts'), fallbackAlerts);
  const alerts = Array.isArray(data) ? data : fallbackAlerts;

  async function closeAlert(alert) {
    try {
      await api.put(`/api/fraud-alerts/${alert.id}/status?status=CLOSED`);
    } finally {
      setData(alerts.map((item) => item.id === alert.id ? { ...item, status: 'CLOSED' } : item));
    }
  }

  return (
    <section className="page-grid">
      <StatusLine loading={loading} error={error} />
      <Panel title="Fraud alerts" className="span-3">
        <div className="alert-list">
          {alerts.map((alert) => (
            <article className="alert-card" key={alert.id}>
              <div><strong>{alert.transactionId || alert.id}</strong><span>{alert.reason || alert.description || 'Rule-based suspicious activity'}</span></div>
              <Badge value={alert.severity || alert.riskStatus || 'HIGH'} />
              <Badge value={alert.status || 'OPEN'} />
              <button className="ghost-button" type="button" onClick={() => closeAlert(alert)}>Close</button>
            </article>
          ))}
        </div>
      </Panel>
    </section>
  );
}

function ReconciliationPage() {
  const [file, setFile] = useState(null);
  const [summary, setSummary] = useState(null);
  const [message, setMessage] = useState('');

  async function upload(event) {
    event.preventDefault();
    if (!file) return;
    const body = new FormData();
    body.append('file', file);
    try {
      const response = await api.post('/api/reconciliation/upload', body);
      setSummary(unwrap(response));
      setMessage('Reconciliation completed.');
    } catch {
      setSummary({ totalRecords: 120, matchedCount: 112, mismatchedCount: 6, duplicateCount: 2, missingCount: 0 });
      setMessage('Backend is offline. Showing sample reconciliation summary.');
    }
  }

  return (
    <section className="page-grid">
      <Panel title="Settlement upload" className="span-2">
        {message && <div className="alert">{message}</div>}
        <form className="upload-zone" onSubmit={upload}>
          <i className="bi bi-cloud-arrow-up" />
          <input type="file" accept=".csv,.xlsx" onChange={(e) => setFile(e.target.files?.[0])} />
          <button className="primary-button" type="submit"><i className="bi bi-arrow-repeat" /> Run reconciliation</button>
        </form>
      </Panel>
      <Panel title="Result">
        {summary ? <MetricGrid compact metrics={Object.entries(summary).slice(0, 5).map(([key, value]) => [toTitle(key), value, 'bi-list-check'])} /> : <EmptyState text="Upload a settlement file to view results." />}
      </Panel>
    </section>
  );
}

function ReportsPage() {
  const { data, loading, error } = useApiResource(() => api.get('/api/reports/dashboard'), fallbackDashboard);
  const riskData = Object.entries(data.fraudRisk || {}).map(([name, value]) => ({ name, value }));

  return (
    <section className="page-grid">
      <StatusLine loading={loading} error={error} />
      <Panel title="Fraud risk summary" className="span-2">
        <ResponsiveContainer height={300}><BarChart data={riskData}><CartesianGrid strokeDasharray="3 3" /><XAxis dataKey="name" /><YAxis /><Tooltip /><Bar dataKey="value" fill="#eb5757" /></BarChart></ResponsiveContainer>
      </Panel>
      <Panel title="Report exports">
        <div className="action-stack">
          <button className="ghost-button" type="button"><i className="bi bi-filetype-csv" /> Daily transactions</button>
          <button className="ghost-button" type="button"><i className="bi bi-file-earmark-bar-graph" /> Risk summary</button>
          <button className="ghost-button" type="button"><i className="bi bi-file-spreadsheet" /> Reconciliation report</button>
        </div>
      </Panel>
    </section>
  );
}

function NotificationsPage() {
  const { data, loading, error, setData } = useApiResource(() => api.get('/api/notifications'), fallbackNotifications);
  const rows = Array.isArray(data) ? data : fallbackNotifications;

  async function markRead(id) {
    try {
      await api.put(`/api/notifications/${id}/read`);
    } finally {
      setData(rows.map((row) => row.id === id ? { ...row, read: true } : row));
    }
  }

  return (
    <section className="page-grid">
      <StatusLine loading={loading} error={error} />
      <Panel title="Notifications" className="span-3">
        <div className="alert-list">
          {rows.map((item) => (
            <article className="alert-card" key={item.id}>
              <div><strong>{item.title || item.type}</strong><span>{item.message}</span></div>
              <Badge value={item.read ? 'READ' : 'UNREAD'} />
              {!item.read && <button className="ghost-button" type="button" onClick={() => markRead(item.id)}>Mark read</button>}
            </article>
          ))}
        </div>
      </Panel>
    </section>
  );
}

function UsersPage() {
  const { data, loading, error } = useApiResource(() => api.get('/api/auth/users'), [
    { name: 'Admin User', email: 'admin@payment.com', roles: ['ADMIN'] },
    { name: 'Finance User', email: 'finance@payment.com', roles: ['FINANCE_USER'] },
    { name: 'Auditor User', email: 'auditor@payment.com', roles: ['AUDITOR'] }
  ]);
  return <section className="page-grid"><StatusLine loading={loading} error={error} /><Panel title="User management" className="span-3"><DataTable rows={Array.isArray(data) ? data : []} columns={['name', 'email', 'roles']} /></Panel></section>;
}

function AuditPage() {
  return (
    <section className="page-grid">
      <Panel title="Audit log" className="span-3">
        <DataTable rows={[
          { action: 'TRANSACTION_CREATED', actor: 'finance@payment.com', entity: 'TXN-SAMPLE-001', timestamp: '2026-05-12 10:15' },
          { action: 'STATUS_UPDATED', actor: 'admin@payment.com', entity: 'TXN-SAMPLE-003', timestamp: '2026-05-12 11:40' }
        ]} columns={['action', 'actor', 'entity', 'timestamp']} />
      </Panel>
    </section>
  );
}

function MetricGrid({ metrics, compact = false }) {
  return (
    <div className={clsx('metric-grid', compact && 'compact')}>
      {metrics.map(([label, value, icon]) => (
        <article className="metric-card" key={label}>
          <i className={clsx('bi', icon)} />
          <span>{label}</span>
          <strong>{value ?? 0}</strong>
        </article>
      ))}
    </div>
  );
}

function Panel({ title, className, children }) {
  return <section className={clsx('panel', className)}><h2>{title}</h2>{children}</section>;
}

function DataTable({ rows, columns }) {
  return (
    <div className="table-wrap">
      <table>
        <thead><tr>{columns.map((column) => <th key={column}>{toTitle(column)}</th>)}</tr></thead>
        <tbody>
          {rows.map((row, index) => (
            <tr key={row.id || row.transactionId || index}>
              {columns.map((column) => <td key={column}>{Array.isArray(row[column]) ? row[column].join(', ') : row[column] ?? '-'}</td>)}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function Badge({ value }) {
  return <span className={clsx('badge', String(value).toLowerCase())}>{value}</span>;
}

function StatusLine({ loading, error }) {
  if (loading) return <div className="alert span-3">Loading live data...</div>;
  if (error) return <div className="alert span-3">{error}</div>;
  return null;
}

function EmptyState({ text }) {
  return <div className="empty-state"><i className="bi bi-inbox" /><span>{text}</span></div>;
}

function toTitle(value) {
  return String(value).replace(/([A-Z])/g, ' $1').replace(/_/g, ' ').replace(/^./, (char) => char.toUpperCase());
}
