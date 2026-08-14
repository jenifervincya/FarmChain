import { useState } from 'react';
import { registerBatch, getAuctionStatus, getTransactionStatus } from '../api/api';
import FairPriceChart from '../components/FairPriceChart';
import StatusBadge from '../components/StatusBadge';

export default function FarmerDashboard() {
  const [form, setForm] = useState({ farmerId: 'F1023', crop: 'tomato', quantityKg: '', region: 'coimbatore' });
  const [batch, setBatch] = useState(null);
  const [auction, setAuction] = useState(null);
  const [transaction, setTransaction] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      const result = await registerBatch({ ...form, quantityKg: Number(form.quantityKg) });
      setBatch(result);
      const [auctionStatus, txn] = await Promise.all([
        getAuctionStatus(result.batchId),
        getTransactionStatus(result.batchId),
      ]);
      setAuction(auctionStatus);
      setTransaction(txn);
    } catch (err) {
      setError(err?.response?.data?.message || err.message || 'Registration failed — check the backend connection.');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <h1>Register a batch</h1>
      <p className="muted">Step 1 of the FairChain journey — this publishes a batch-registered event.</p>

      <div className="card" style={{ marginTop: 24 }}>
        <form onSubmit={handleSubmit}>
          <div className="field-row">
            <label htmlFor="crop">Crop</label>
            <input id="crop" value={form.crop} onChange={(e) => setForm({ ...form, crop: e.target.value })} />
          </div>
          <div className="field-row">
            <label htmlFor="quantity">Quantity (kg)</label>
            <input
              id="quantity"
              type="number"
              value={form.quantityKg}
              onChange={(e) => setForm({ ...form, quantityKg: e.target.value })}
              required
            />
          </div>
          <div className="field-row">
            <label htmlFor="region">Region</label>
            <input id="region" value={form.region} onChange={(e) => setForm({ ...form, region: e.target.value })} />
          </div>
          <button className="primary" type="submit" disabled={submitting}>
            {submitting ? 'Registering…' : 'Register batch'}
          </button>
          {error && (
            <p className="stamp alert" style={{ marginTop: 12, display: 'block', width: 'fit-content' }}>
              {error}
            </p>
          )}
        </form>
      </div>

      {batch && (
        <div className="card">
          <h3>
            Batch <span className="tracking-code">{batch.trackingCode}</span>
          </h3>
          <p className="muted">
            Batch ID (use this on the Buyer Dashboard to bid): <span className="tracking-code">{batch.batchId}</span>
          </p>
          <p className="muted">Registered {new Date(batch.createdAt).toLocaleString()}</p>
          <StatusBadge status={batch.status} />

          <div style={{ marginTop: 24 }}>
            <h3 style={{ fontSize: '1rem', marginBottom: 12 }}>Fair price band</h3>
            <FairPriceChart crop={form.crop} region={form.region} />
          </div>

          {auction && (
            <div style={{ marginTop: 24 }}>
              <h3 style={{ fontSize: '1rem', marginBottom: 12 }}>Auction status</h3>
              <p>
                Floor price: <strong>₹{auction.floorPrice}</strong> · Status: <StatusBadge status={auction.status} />
              </p>
              <table className="ledger">
                <thead>
                  <tr>
                    <th>Bid</th>
                    <th>Buyer</th>
                    <th>Amount</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {(auction.bids || []).map((bid) => (
                    <tr key={bid.bidId}>
                      <td>{bid.bidId}</td>
                      <td>{bid.buyerId}</td>
                      <td>₹{bid.amount}</td>
                      <td>
                        <StatusBadge status={bid.status} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          <div style={{ marginTop: 24 }}>
            <h3 style={{ fontSize: '1rem', marginBottom: 12 }}>Payment status</h3>
            {transaction ? (
              <p>
                <StatusBadge status={transaction.escrowStatus} />{' '}
                {transaction.escrowStatus === 'RELEASED' ? (
                  <>
                    — ₹{transaction.amount} released
                    {transaction.releasedAt && ` on ${new Date(transaction.releasedAt).toLocaleString()}`}
                  </>
                ) : (
                  <>— ₹{transaction.amount} held in escrow, releases automatically on delivery confirmation</>
                )}
              </p>
            ) : (
              <p className="muted">Payment status not available yet for this batch.</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
