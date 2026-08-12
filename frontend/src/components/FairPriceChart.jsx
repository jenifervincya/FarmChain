import { useEffect, useState } from 'react';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
} from 'chart.js';
import { getFairPriceBand } from '../api/api';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Filler, Tooltip);

/**
 * Renders the fair price band for a crop/region, sourced from
 * GET /ai/fair-price-band (called through Backend's proxy per Section 4.1 —
 * this component never talks to the AI service directly).
 */
export default function FairPriceChart({ crop, region, currentPrice }) {
  const [band, setBand] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    getFairPriceBand({ crop, region }).then((data) => {
      if (!cancelled) {
        setBand(data);
        setLoading(false);
      }
    });
    return () => {
      cancelled = true;
    };
  }, [crop, region]);

  if (loading) return <p className="muted">Loading fair price band…</p>;
  if (!band) return null;

  const labels = ['Floor', 'Fair band', 'Ceiling'];
  const data = {
    labels,
    datasets: [
      {
        label: 'Fair price band (₹/kg)',
        data: [band.minPrice, (band.minPrice + band.maxPrice) / 2, band.maxPrice],
        borderColor: '#a9791f',
        backgroundColor: 'rgba(169, 121, 31, 0.12)',
        fill: true,
        tension: 0.35,
      },
      ...(currentPrice
        ? [
            {
              label: 'Current bid/price',
              data: [currentPrice, currentPrice, currentPrice],
              borderColor: '#3e6b4a',
              borderDash: [4, 4],
              pointRadius: 0,
            },
          ]
        : []),
    ],
  };

  const options = {
    responsive: true,
    plugins: { legend: { display: true, position: 'bottom' } },
    scales: { y: { title: { display: true, text: band.currency } } },
  };

  return (
    <div>
      <Line data={data} options={options} />
      <p className="muted">
        Computed {new Date(band.computedAt).toLocaleString()} · weather:{' '}
        {band.factors?.weatherImpact} · transport index: {band.factors?.transportCostIndex} ·
        sentiment: {band.factors?.crisisSentiment}
      </p>
    </div>
  );
}
