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

export default {
  registerBatch,
  getBatchStatus,
  placeBid,
  getAuctionStatus,
  logEvent,
  getReputation,
  getPriceJourney,
  getFairPriceBand,
};
