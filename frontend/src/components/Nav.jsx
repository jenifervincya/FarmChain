import { NavLink } from 'react-router-dom';

export default function Nav() {
  return (
    <header className="app-header">
      <div className="container">
        <div className="brand">
          Fair<span className="brand-mark">Chain</span>
        </div>
        <nav>
          <NavLink to="/farmer" className={({ isActive }) => (isActive ? 'active' : '')}>
            Farmer
          </NavLink>
          <NavLink to="/buyer" className={({ isActive }) => (isActive ? 'active' : '')}>
            Buyer
          </NavLink>
          <NavLink to="/track" className={({ isActive }) => (isActive ? 'active' : '')}>
            Track a batch
          </NavLink>
          <NavLink to="/notifications" className={({ isActive }) => (isActive ? 'active' : '')}>
            Notifications
          </NavLink>
        </nav>
      </div>
    </header>
  );
}
