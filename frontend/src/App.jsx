import { Routes, Route, Navigate } from 'react-router-dom';
import Nav from './components/Nav';
import FarmerDashboard from './pages/FarmerDashboard';
import BuyerDashboard from './pages/BuyerDashboard';
import CustomerJourney from './pages/CustomerJourney';

export default function App() {
  return (
    <>
      <Nav />
      <main className="container">
        <Routes>
          <Route path="/" element={<Navigate to="/farmer" replace />} />
          <Route path="/farmer" element={<FarmerDashboard />} />
          <Route path="/buyer" element={<BuyerDashboard />} />
          <Route path="/track" element={<CustomerJourney />} />
        </Routes>
      </main>
    </>
  );
}
