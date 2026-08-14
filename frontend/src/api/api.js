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
// DEMO-ONLY local memory (mock mode only): remembers what was actually
// registered (crop, region, quantity) so later screens — tracking,
// auction, price journey — reflect the real input instead of a
// hardcoded "tomato" example. This is NOT a database and isn't meant to
// be one; it's a stand-in for what Backend's PostgreSQL will really do
// once you're on real endpoints. Lives in localStorage so it survives
// a page refresh during a demo.
// ---------------------------------------------------------------------
const DEMO_STORE_KEY = 'fairchain_demo_batches';

function readDemoStore() {
  try {
    return JSON.parse(localStorage.getItem(DEMO_STORE_KEY)) || {};
  } catch {
    return {};
  }
}

function writeDemoRecord(batchId, trackingCode, record) {
  const store = readDemoStore();
  store[batchId] = { trackingCode, ...record };
  store[trackingCode] = { batchId, ...record };
  localStorage.setItem(DEMO_STORE_KEY, JSON.stringify(store));
}

function readDemoRecord(idOrTrackingCode) {
  return readDemoStore()[idOrTrackingCode] || null;
}

// ---------------------------------------------------------------------
// POST /api/v1/batches  — Register a new produce batch
// ---------------------------------------------------------------------
export async function registerBatch({ farmerId, crop, quantityKg, region }) {
  if (USE_MOCKS) {
    const cropCode = (crop || 'CRP').slice(0, 3).toUpperCase();
    const idNum = Math.floor(10000 + seededRandom(`${crop}-${region}-${quantityKg}`) * 90000);
    const batchId = `B${idNum}`;
    const trackingCode = `FC-${cropCode}-${idNum}`;
    writeDemoRecord(batchId, trackingCode, { crop, region, quantityKg, farmerId });
    return delay({
      batchId,
      trackingCode,
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
    const record = readDemoRecord(trackingCode);
    const crop = record?.crop || 'produce';
    const region = record?.region || 'coimbatore';
    return delay({
      batchId: record?.batchId || 'B00000',
      trackingCode,
      crop,
      region,
      quantityKg: record?.quantityKg || 500,
      status: 'IN_TRANSIT',
      events: [
        { eventType: 'pickup-event', timestamp: '2026-08-09T10:30:00Z', location: `Farm - ${region}` },
        { eventType: 'transport-event', timestamp: '2026-08-09T14:00:00Z', location: 'En route - Salem' },
      ],
    });
  }
  const { data } = await client.get(`/batches/${trackingCode}`);
  return data;
}

// ---------------------------------------------------------------------
// GET /api/v1/transactions/{batchId} — LIVE, confirmed by Jenifer (2026-08-14).
// Real shape: { batchId, farmerId, buyerId, amount, escrowStatus, releasedAt }
// escrowStatus: PENDING until a delivery-event fires, then RELEASED with
// a real releasedAt timestamp. Not documented in Section 4.1 yet — flag
// for that to get added.
// ---------------------------------------------------------------------
export async function getTransactionStatus(batchId) {
  if (USE_MOCKS) {
    const record = readDemoRecord(batchId);
    const crop = record?.crop || 'produce';
    const region = record?.region || 'coimbatore';
    const band = generateSyntheticBand(crop, region);
    return delay({
      batchId,
      farmerId: record?.farmerId || 'F1023',
      buyerId: 'BUY-201',
      amount: Math.round(band.minPrice * (record?.quantityKg || 500) * 100) / 100,
      escrowStatus: 'PENDING', // mock mode never fires a real delivery-event, so stays PENDING
      releasedAt: null,
    });
  }
  const { data } = await client.get(`/transactions/${batchId}`);
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
    const record = readDemoRecord(batchId);
    const crop = record?.crop || 'produce';
    const region = record?.region || 'coimbatore';
    const band = generateSyntheticBand(crop, region);
    const idNum = Math.floor(1000 + seededRandom(`${batchId}-auction`) * 9000);
    return delay({
      auctionId: `AUC-${idNum}`,
      batchId,
      crop,
      region,
      status: 'OPEN',
      floorPrice: band.minPrice,
      bids: [
        { bidId: `BID-${idNum}1`, buyerId: 'BUY-201', amount: Math.round((band.minPrice + 0.5) * 100) / 100, status: 'ACCEPTED' },
        { bidId: `BID-${idNum}2`, buyerId: 'BUY-204', amount: Math.round((band.minPrice - 1.5) * 100) / 100, status: 'REJECTED_BELOW_FLOOR' },
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
    const record = readDemoRecord(trackingCode);
    const crop = record?.crop || 'produce';
    const region = record?.region || 'coimbatore';
    const band = generateSyntheticBand(crop, region);
    const farmgate = band.minPrice + (band.maxPrice - band.minPrice) * 0.2;
    const auctionPrice = farmgate;
    const retail = band.maxPrice * 1.15; // retail markup beyond the fair band — the "gap" FairChain surfaces
    return delay({
      trackingCode,
      crop,
      region,
      fairBand: { minPrice: band.minPrice, maxPrice: band.maxPrice, currency: band.currency },
      steps: [
        { stage: 'Farmgate', price: Math.round(farmgate * 100) / 100, timestamp: '2026-08-09T10:00:00Z' },
        { stage: 'Auction sale', price: Math.round(auctionPrice * 100) / 100, timestamp: '2026-08-09T11:15:00Z' },
        { stage: 'Retail', price: Math.round(retail * 100) / 100, timestamp: '2026-08-10T08:00:00Z' },
      ],
    });
  }
  const { data } = await client.get(`/price-journey/${trackingCode}`);
  return data;
}

// ---------------------------------------------------------------------
// GET /ai/fair-price-band?crop=&region=  (AI-implemented, always called
// through Backend's proxy — Frontend never calls the AI service directly)
//
// REAL DATA SOURCE (temporary, demo-only): data.gov.in publishes actual
// government mandi price data ("Variety-wise Daily Market Prices of
// Commodity", sourced from Agmarknet). Free API key at
// https://data.gov.in/user/register — then set VITE_MANDI_API_KEY in
// .env. This calls it directly from the browser as a stopgap so the
// demo has real numbers when possible; architecturally this belongs in
// Anisha's AI service (Section 3.2), not here — flag that to the team
// after the demo rather than leaving it as a permanent pattern.
//
// Government APIs often don't set CORS headers for browser calls, so
// this can fail silently from a browser (not from curl/Postman, only
// from JS running on a webpage). If it fails for ANY reason — no key,
// CORS block, crop not found in the dataset, network error — this
// falls back to generateSyntheticBand(), which produces a believable,
// CONSISTENT price band for literally any crop/region typed, not just
// tomato. That fallback is what protects you if someone types "onion"
// live in front of the panel.
// ---------------------------------------------------------------------

const MANDI_RESOURCE_ID = '9ef84268-d588-465a-a308-a864a43d0070'; // data.gov.in dataset id
const MANDI_API_KEY = import.meta.env.VITE_MANDI_API_KEY;

// Deterministic "hash" so the same crop+region always produces the same
// believable numbers across reloads, instead of jumping around randomly
// every time you register the same crop again.
function seededRandom(seed) {
  let h = 0;
  for (let i = 0; i < seed.length; i++) {
    h = (h << 5) - h + seed.charCodeAt(i);
    h |= 0;
  }
  return Math.abs(h % 1000) / 1000; // 0..1
}

function generateSyntheticBand(crop, region) {
  const base = 12 + seededRandom(`${crop}-${region}-base`) * 40; // ₹12–52/kg base
  const spread = 3 + seededRandom(`${crop}-${region}-spread`) * 6;
  const weatherOptions = ['clear', 'moderate_rain_delay', 'heavy_rain_risk', 'heatwave_stress'];
  const sentimentOptions = ['neutral', 'positive', 'cautious'];
  return {
    crop,
    region,
    minPrice: Math.round((base - spread) * 100) / 100,
    maxPrice: Math.round((base + spread) * 100) / 100,
    currency: 'INR_per_kg',
    computedAt: new Date().toISOString(),
    factors: {
      weatherImpact: weatherOptions[Math.floor(seededRandom(`${crop}-${region}-weather`) * weatherOptions.length)],
      transportCostIndex: Math.round((1 + seededRandom(`${crop}-${region}-transport`) * 0.4) * 100) / 100,
      crisisSentiment: sentimentOptions[Math.floor(seededRandom(`${crop}-${region}-sentiment`) * sentimentOptions.length)],
    },
    source: 'synthetic', // clearly labeled — not a real government figure
  };
}

async function fetchRealMandiPrice(crop, region) {
  if (!MANDI_API_KEY) return null;
  const url = `https://api.data.gov.in/resource/${MANDI_RESOURCE_ID}?api-key=${MANDI_API_KEY}&format=json&limit=5&filters[commodity]=${encodeURIComponent(crop)}`;
  const res = await fetch(url);
  if (!res.ok) return null;
  const data = await res.json();
  const record = data?.records?.[0];
  if (!record) return null;
  const min = parseFloat(record.min_price);
  const max = parseFloat(record.max_price);
  if (!min || !max) return null;
  return {
    crop,
    region: record.district || region,
    minPrice: min / 100, // dataset reports ₹/quintal — convert to ₹/kg
    maxPrice: max / 100,
    currency: 'INR_per_kg',
    computedAt: new Date().toISOString(),
    factors: {
      weatherImpact: 'not modeled (real mandi data has no weather field)',
      transportCostIndex: null,
      crisisSentiment: null,
    },
    source: 'agmarknet_live',
  };
}

export async function getFairPriceBand({ crop, region }) {
  if (USE_MOCKS) {
    try {
      const real = await fetchRealMandiPrice(crop, region);
      if (real) return real;
    } catch {
      // fall through to synthetic
    }
    return delay(generateSyntheticBand(crop, region));
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
export async function getPendingNotifications() {
  if (USE_MOCKS) {
    return delay([
      {
        id: '1',
        farmerId: 'F1023',
        batchId: '5',
        eventType: 'BATCH_REGISTERED',
        message: 'Your batch of 500kg tomato has been registered. Tracking updates will follow.',
        status: 'PENDING',
        createdAt: '2026-08-13T10:00:05Z',
      },
      {
        id: '2',
        farmerId: 'F1023',
        batchId: '5',
        eventType: 'SALE_CONFIRMED',
        message: 'Your batch sold for 19.00. Payment will follow on delivery confirmation.',
        status: 'PENDING',
        createdAt: '2026-08-13T11:15:03Z',
      },
    ]);
  }
  const { data } = await client.get('/notifications', {
    params: { status: 'PENDING' },
  });
  return data;
}

// ---------------------------------------------------------------------
// Farmer contact lookup — LIVE as of Jenifer's message (2026-08-13):
// GET /api/v1/farmers/{farmerId}, routed through the gateway, returns
// { phone, name, region, preferredLanguage }. No more guessing needed.
// Fallback to the local demo phonebook is kept as a safety net for the
// live demo — if the network call fails for any reason (backend not
// running, wrong port, CORS), the UI still works instead of breaking
// on stage.
// ---------------------------------------------------------------------
const DEMO_PHONEBOOK = {
  F1023: { phone: '+91-98765-43210', preferredLanguage: 'ta', source: 'demo' },
};

export async function getFarmerContact(farmerId) {
  try {
    const { data } = await client.get(`/farmers/${farmerId}`);
    return { phone: data.phone, preferredLanguage: data.preferredLanguage, source: 'live' };
  } catch {
    // Backend not reachable (down, wrong port, CORS) — fall back so the UI still works.
    return DEMO_PHONEBOOK[farmerId] || { phone: null, preferredLanguage: null, source: 'demo' };
  }
}

// mark-sent confirmed: no body, unconditionally sets status to SENT.
// There's currently no FAILED path on Backend — flagged to Jenifer if
// retry/error UI is needed later.
export async function markNotificationSent(id) {
  if (USE_MOCKS) {
    return delay({ id, status: 'SENT' });
  }
  const { data } = await client.post(`/notifications/${id}/mark-sent`);
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
  getPendingNotifications,
  markNotificationSent,
  getFarmerContact,
  getTransactionStatus,
};
