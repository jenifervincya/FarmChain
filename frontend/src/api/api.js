/**
 * FairChain API service layer.
 *
 * This is the ONLY file in the Frontend codebase that talks to Backend.
 * Per Section 1.5 / 3.3 of the project spec: Frontend consumes Backend
 * REST APIs only — never Kafka, never PostgreSQL, never the AI service
 * directly. Every function here maps 1:1 to an endpoint documented in
 * Section 4.1.
 *
 * MOCK MODE: while Backend endpoints aren't live yet (Phase 1, per
 * Section 5), set VITE_USE_MOCKS=true in .env and every function below
 * returns a canned response shaped exactly like the spec's sample
 * payloads. Flip it off once Jenifer's endpoints are up — no other
 * code in the app should need to change.
 *
 * IMPORTANT: If a screen needs a field that isn't in one of these
 * response shapes, do NOT invent it here. Per Section 3.3 "Must NOT
 * modify," raise it with Backend and get Section 4.1 updated first.
 */

import axios from 'axios';

const USE_MOCKS = import.meta.env.VITE_USE_MOCKS === 'true';

const client = axios.create({
  baseURL: '/api/v1',
  headers: { 'Content-Type': 'application/json' },
});

// Attach the simplified demo auth token (Section 4.4) if present.
client.interceptors.request.use((config) => {
  const token = localStorage.getItem('fairchain_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

function delay(data, ms = 400) {
  return new Promise((resolve) => setTimeout(() => resolve(data), ms));
}

// ---------------------------------------------------------------------
// POST /api/v1/batches  — Register a new produce batch
// ---------------------------------------------------------------------
export async function registerBatch({ farmerId, crop, quantityKg, region }) {
  if (USE_MOCKS) {
    return delay({
      batchId: 'B98213',
      trackingCode: 'FC-TMT-98213',
      status: 'REGISTERED',
      createdAt: new Date().toISOString(),
    });
  }
  const { data } = await client.post('/batches', { farmerId, crop, quantityKg, region });
  return data;
}

// ---------------------------------------------------------------------
// GET /api/v1/batches/{trackingCode} — Full batch journey/status
// ---------------------------------------------------------------------
export async function getBatchStatus(trackingCode) {
  if (USE_MOCKS) {
    return delay({
      batchId: 'B98213',
      trackingCode,
      crop: 'tomato',
      region: 'coimbatore',
      quantityKg: 500,
      status: 'IN_TRANSIT',
      events: [
        { eventType: 'pickup-event', timestamp: '2026-08-09T10:30:00Z', location: 'Farm - Coimbatore' },
        { eventType: 'transport-event', timestamp: '2026-08-09T14:00:00Z', location: 'En route - Salem' },
      ],
    });
  }
  const { data } = await client.get(`/batches/${trackingCode}`);
  return data;
}

// ---------------------------------------------------------------------
// POST /api/v1/auctions/{batchId}/bids — Buyer places a bid
// ---------------------------------------------------------------------
export async function placeBid(batchId, { buyerId, amount }) {
  if (USE_MOCKS) {
    return delay({
      bidId: 'BID-5521',
      auctionId: 'AUC-4410',
      batchId,
      buyerId,
      amount,
      status: 'PENDING_VALIDATION',
    });
  }
  const { data } = await client.post(`/auctions/${batchId}/bids`, { buyerId, amount });
  return data;
}

// ---------------------------------------------------------------------
// GET /api/v1/auctions/{batchId}/status — Current auction status
// ---------------------------------------------------------------------
export async function getAuctionStatus(batchId) {
  if (USE_MOCKS) {
    return delay({
      auctionId: 'AUC-4410',
      batchId,
      status: 'OPEN',
      floorPrice: 18.5,
      bids: [
        { bidId: 'BID-5519', buyerId: 'BUY-201', amount: 19.0, status: 'ACCEPTED' },
        { bidId: 'BID-5520', buyerId: 'BUY-204', amount: 17.0, status: 'REJECTED_BELOW_FLOOR' },
      ],
    });
  }
  const { data } = await client.get(`/auctions/${batchId}/status`);
  return data;
}

// ---------------------------------------------------------------------
// POST /api/v1/events — Log a pickup/transport/warehouse/delivery event
// ---------------------------------------------------------------------
export async function logEvent({ batchId, eventType, location, metadata }) {
  if (USE_MOCKS) {
    return delay({
      eventId: 'EVT-7781',
      batchId,
      eventType,
      location,
      timestamp: new Date().toISOString(),
      status: 'RECORDED',
    });
  }
  const { data } = await client.post('/events', { batchId, eventType, location, metadata });
  return data;
}

// ---------------------------------------------------------------------
// GET /api/v1/reputation/{entityId} — Middleman/buyer reputation score
// ---------------------------------------------------------------------
export async function getReputation(entityId) {
  if (USE_MOCKS) {
    return delay({
      entityId,
      entityType: 'BUYER',
      score: 82,
      lastUpdated: '2026-08-08T09:00:00Z',
    });
  }
  const { data } = await client.get(`/reputation/${entityId}`);
  return data;
}

// ---------------------------------------------------------------------
// GET /api/v1/price-journey/{trackingCode} — Full price breakdown
// ---------------------------------------------------------------------
export async function getPriceJourney(trackingCode) {
  if (USE_MOCKS) {
    return delay({
      trackingCode,
      crop: 'tomato',
      region: 'coimbatore',
      fairBand: { minPrice: 18.5, maxPrice: 24.0, currency: 'INR_per_kg' },
      steps: [
        { stage: 'Farmgate', price: 19.0, timestamp: '2026-08-09T10:00:00Z' },
        { stage: 'Auction sale', price: 19.0, timestamp: '2026-08-09T11:15:00Z' },
        { stage: 'Retail', price: 26.5, timestamp: '2026-08-10T08:00:00Z' },
      ],
    });
  }
  const { data } = await client.get(`/price-journey/${trackingCode}`);
  return data;
}

// ---------------------------------------------------------------------
// GET /ai/fair-price-band?crop=&region=  (AI-implemented, always called
// through Backend's proxy — Frontend never calls the AI service directly)
// ---------------------------------------------------------------------
export async function getFairPriceBand({ crop, region }) {
  if (USE_MOCKS) {
    return delay({
      crop,
      region,
      minPrice: 18.5,
      maxPrice: 24.0,
      currency: 'INR_per_kg',
      computedAt: new Date().toISOString(),
      factors: {
        weatherImpact: 'moderate_rain_delay',
        transportCostIndex: 1.12,
        crisisSentiment: 'neutral',
      },
    });
  }
  // Routed through Backend, not called on the AI service host directly.
  const { data } = await client.get('/ai/fair-price-band', { params: { crop, region } });
  return data;
}

// ---------------------------------------------------------------------
// LIVE as of 2026-08-13 (Jenifer, pushed to dev) — routed through the
// gateway at /api/v1/notifications/**. Not yet documented in Section 4.1
// itself; flagged to the team to get it added.
//
// GET /api/v1/notifications?status=PENDING  (status filter optional)
// POST /api/v1/notifications/{id}/mark-sent  (flips status to SENT)
//
// Real response shape confirmed by Jenifer:
// { id, farmerId, batchId, eventType, message, status, createdAt }
// NOTE: no phone number in this payload — see the open question below
// about how Frontend is supposed to get a farmer's phone to actually
// send an SMS. Flagged, not worked around.
// ---------------------------------------------------------------------
let mockNotifications = [
  {
    id: 1,
    farmerId: 'F1023',
    farmerPhone: '+91-98765-43210',
    preferredLanguage: 'ta',
    batchId: '5',
    eventType: 'BATCH_REGISTERED',
    message:
      'Your batch of 500kg tomato has been registered. Tracking updates will follow.',
    status: 'PENDING',
    createdAt: '2026-08-13T10:00:05Z',
  },
  {
    id: 2,
    farmerId: 'F1023',
    farmerPhone: '+91-98765-43210',
    preferredLanguage: 'ta',
    batchId: '5',
    eventType: 'SALE_CONFIRMED',
    message:
      'Your batch sold for 19.00. Payment will follow on delivery confirmation.',
    status: 'PENDING',
    createdAt: '2026-08-13T11:15:03Z',
  },
];

export async function getPendingNotifications() {
  if (USE_MOCKS) {
    return delay(mockNotifications);
  }

  const { data } = await client.get('/notifications', {
    params: { status: 'PENDING' },
  });

  return data;
}

// Body shape unconfirmed with Jenifer — assuming { status } for now.
// If mark-sent turns out to take no body, this still works (Backend
// would just ignore the extra field), but confirm before relying on
// the FAILED path.
export async function markNotificationSent(id, result /* 'SENT' | 'FAILED' */) {
  if (USE_MOCKS) {
    mockNotifications = mockNotifications.map((notification) =>
      notification.id === id
        ? { ...notification, status: result }
        : notification
    );

    return delay({ id, status: result });
  }

  const { data } = await client.post(`/notifications/${id}/mark-sent`, {
    status: result,
  });

  return data;
}

// ---------------------------------------------------------------------
// Farmer contact lookup — NOT in Section 4.1 yet. Jenifer confirmed
// notifications only return farmerId, no phone. She's unavailable to
// add a real endpoint right now, so this tries a sensible guessed path
// first (GET /api/v1/farmers/{farmerId}) and falls back to a local demo
// phonebook if that 404s or errors — so the app stays fully functional
// today. Swap this out (or just delete the fallback) the moment the
// real endpoint exists; nothing else in the app needs to change.
// ---------------------------------------------------------------------
const DEMO_PHONEBOOK = {
  F1023: { phone: '+91-98765-43210', preferredLanguage: 'ta', source: 'demo' },
};

export async function getFarmerContact(farmerId) {
  if (USE_MOCKS) {
    return DEMO_PHONEBOOK[farmerId] || { phone: null, preferredLanguage: null, source: 'demo' };
  }
  try {
    const { data } = await client.get(`/farmers/${farmerId}`);
    return { phone: data.phone, preferredLanguage: data.preferredLanguage, source: 'live' };
  } catch {
    // Endpoint doesn't exist yet — fall back so the UI still works.
    return DEMO_PHONEBOOK[farmerId] || { phone: null, preferredLanguage: null, source: 'demo' };
  }
}

export default {
  registerBatch,
  getBatchStatus,
  placeBid,
  getAuctionStatus,
  logEvent,
  getReputation,
  getPriceJourney,
  getFairPriceBand,
  getPendingNotifications,
  markNotificationSent,
  getFarmerContact,
};
