import { useState } from 'react';
import { placeBid, getReputation } from '../api/api';
import FairPriceChart from '../components/FairPriceChart';
import StatusBadge from '../components/StatusBadge';

export default function BuyerDashboard() {
  const [buyerId] = useState('BUY-201');
  const [batchId, setBatchId] = useState('');
  const [amount, setAmount] = useState('');
  const [bidResult, setBidResult] = useState(null);
  const [reputation, setReputation] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleBid(e) {
    e.preventDefault();
    setSubmitting(true);
    const result = await placeBid(batchId, { buyerId, amount: Number(amount) });
    setBidResult(result);
    setSubmitting(false);
  }

  async function loadReputation() {
    const rep = await getReputation(buyerId);
    setReputation(rep);
  }

  return (
    <div>
      <h1>Place a bid</h1>
      <p className="muted">Bids below the AI fair-price floor are auto-rejected by the auction service.</p>

      <div className="card" style={{ marginTop: 24 }}>
        <form onSubmit={handleBid}>
          <div className="field-row">
            <label htmlFor="batchId">Batch ID</label>
            <input id="batchId" value={batchId} onChange={(e) => setBatchId(e.target.value)} required />
          </div>
          <div className="field-row">
            <label htmlFor="amount">Bid amount (₹/kg)</label>
            <input
              id="amount"
              type="number"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
            />
          </div>
          <button className="primary" type="submit" disabled={submitting}>
            {submitting ? 'Submitting…' : 'Submit bid'}
          </button>
        </form>
      </div>

      {bidResult && (
        <div className="card">
          <p>
            Bid <span className="tracking-code">{bidResult.bidId}</span> —{' '}
            <StatusBadge status={bidResult.status} />
          </p>
          <FairPriceChart crop="tomato" region="coimbatore" currentPrice={Number(amount)} />
        </div>
      )}

      <div className="card">
        <h3 style={{ fontSize: '1rem', marginBottom: 12 }}>My reputation</h3>
        {!reputation ? (
          <button className="primary" onClick={loadReputation}>
            Load reputation score
          </button>
        ) : (
          <p>
            Score: <strong>{reputation.score}</strong> / 100 · last updated{' '}
            {new Date(reputation.lastUpdated).toLocaleDateString()}
          </p>
        )}
      </div>
    </div>
  );
}
