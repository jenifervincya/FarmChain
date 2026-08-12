import { useState } from 'react';
import { getPriceJourney, getBatchStatus } from '../api/api';

export default function CustomerJourney() {
  const [trackingCode, setTrackingCode] = useState('FC-TMT-98213');
  const [journey, setJourney] = useState(null);
  const [batch, setBatch] = useState(null);
  const [loading, setLoading] = useState(false);

  async function handleTrack(e) {
    e.preventDefault();
    setLoading(true);
    const [journeyData, batchData] = await Promise.all([
      getPriceJourney(trackingCode),
      getBatchStatus(trackingCode),
    ]);
    setJourney(journeyData);
    setBatch(batchData);
    setLoading(false);
  }

  return (
    <div>
      <h1>Price Journey Replay</h1>
      <p className="muted">Scan or enter a tracking code to see this batch's full journey and price breakdown.</p>

      <div className="card" style={{ marginTop: 24 }}>
        <form onSubmit={handleTrack}>
          <div className="field-row">
            <label htmlFor="trackingCode">Tracking code</label>
            <input
              id="trackingCode"
              className="tracking-code"
              value={trackingCode}
              onChange={(e) => setTrackingCode(e.target.value)}
              required
            />
          </div>
          <button className="primary" type="submit" disabled={loading}>
            {loading ? 'Looking up…' : 'Track batch'}
          </button>
        </form>
      </div>

      {journey && batch && (
        <div className="card">
          <h3>
            {journey.crop} · {journey.region}
          </h3>
          <p className="muted">
            Fair band: ₹{journey.fairBand.minPrice}–₹{journey.fairBand.maxPrice} {journey.fairBand.currency}
          </p>

          <h3 style={{ fontSize: '1rem', marginTop: 24, marginBottom: 12 }}>Physical journey</h3>
          <ul className="journey-list">
            {batch.events.map((ev, i) => (
              <li key={i}>
                <div className="stage">{ev.eventType}</div>
                <div>{ev.location}</div>
                <div className="muted">{new Date(ev.timestamp).toLocaleString()}</div>
              </li>
            ))}
          </ul>

          <h3 style={{ fontSize: '1rem', marginTop: 24, marginBottom: 12 }}>Price journey</h3>
          <table className="ledger">
            <thead>
              <tr>
                <th>Stage</th>
                <th>Price</th>
                <th>When</th>
              </tr>
            </thead>
            <tbody>
              {journey.steps.map((step, i) => (
                <tr key={i}>
                  <td>{step.stage}</td>
                  <td>₹{step.price}</td>
                  <td>{new Date(step.timestamp).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
