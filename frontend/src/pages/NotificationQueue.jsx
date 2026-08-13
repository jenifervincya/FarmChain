import { useEffect, useState } from 'react';
import { getPendingNotifications, markNotificationSent, getFarmerContact } from '../api/api';
import StatusBadge from '../components/StatusBadge';

/**
 * SMS/IVR send console.
 *
 * Flow per Section 3.1/3.3: Backend's notification-service decides WHEN
 * and WHAT to send (writes notification_log rows, exposed via the live
 * GET /api/v1/notifications endpoint). This screen turns that into an
 * actual SMS/IVR send.
 *
 * PHONE NUMBER: live as of Jenifer's confirmation — GET /api/v1/farmers/{id}
 * returns phone, name, region, preferredLanguage. getFarmerContact() in
 * api.js calls this for real, falling back to a small labeled demo
 * phonebook only if the backend call fails (down, wrong port, CORS) —
 * that fallback protects the live demo from a network hiccup.
 *
 * TWILIO: a Twilio Auth Token can't safely live in browser JS. sendNotification()
 * below is a stub that simulates a send — the real version needs a
 * server-side relay holding the credentials, not a direct call from here.
 */

const CHANNEL_OPTIONS = ['SMS', 'IVR'];

async function sendNotification(row, contact, channel) {
  // STUB — replace with a call to the server-side Twilio relay once it exists.
  console.log(`[stub] would send via ${channel} to ${contact.phone}:`, row.message);
  await new Promise((r) => setTimeout(r, 350));
  return { ok: true };
}

export default function NotificationQueue() {
  const [rows, setRows] = useState([]);
  const [contacts, setContacts] = useState({});
  const [loading, setLoading] = useState(true);
  const [sendingId, setSendingId] = useState(null);
  const [channel, setChannel] = useState('SMS');

  async function load() {
    setLoading(true);
    const data = await getPendingNotifications();
    setRows(data);

    const uniqueFarmerIds = [...new Set(data.map((r) => r.farmerId).filter(Boolean))];
    const contactEntries = await Promise.all(
      uniqueFarmerIds.map(async (id) => [id, await getFarmerContact(id)])
    );
    setContacts(Object.fromEntries(contactEntries));
    setLoading(false);
  }

  useEffect(() => {
    load();
  }, []);

  async function handleSend(row) {
    const contact = contacts[row.farmerId];
    if (!contact?.phone) return;
    setSendingId(row.id);
    await sendNotification(row, contact, channel);
    await markNotificationSent(row.id); // no body — always sets SENT
    setSendingId(null);
    load();
  }

  return (
    <div>
      <h1>Notification queue</h1>
      <p className="muted">
        Pending SMS/IVR notifications from notification-service. Phone numbers marked{' '}
        <strong>DEMO</strong> come from a local fallback, not a live endpoint — see the note in
        api.js.
      </p>

      <div className="card" style={{ marginTop: 24 }}>
        <div className="field-row" style={{ maxWidth: 200 }}>
          <label htmlFor="channel">Send via</label>
          <select id="channel" value={channel} onChange={(e) => setChannel(e.target.value)}>
            {CHANNEL_OPTIONS.map((c) => (
              <option key={c} value={c}>
                {c}
              </option>
            ))}
          </select>
        </div>

        {loading ? (
          <p className="muted">Loading…</p>
        ) : (
          <table className="ledger">
            <thead>
              <tr>
                <th>Event</th>
                <th>Batch</th>
                <th>Message</th>
                <th>Farmer</th>
                <th>Phone</th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => {
                const contact = contacts[row.farmerId];
                return (
                  <tr key={row.id}>
                    <td>{row.eventType}</td>
                    <td className="tracking-code">{row.batchId}</td>
                    <td style={{ fontFamily: 'var(--font-body)', maxWidth: 300 }}>
                      {row.message}
                    </td>
                    <td className="tracking-code">{row.farmerId || '—'}</td>
                    <td className="tracking-code">
                      {contact?.phone || '—'}
                      {contact?.source === 'demo' && (
                        <span className="stamp pending" style={{ marginLeft: 6 }}>
                          demo
                        </span>
                      )}
                    </td>
                    <td>
                      <StatusBadge status={row.status} />
                    </td>
                    <td>
                      {row.status === 'PENDING' && (
                        <button
                          className="primary"
                          onClick={() => handleSend(row)}
                          disabled={sendingId === row.id || !contact?.phone}
                          title={!contact?.phone ? 'No phone number for this farmer' : undefined}
                        >
                          {sendingId === row.id ? 'Sending…' : `Send ${channel}`}
                        </button>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
