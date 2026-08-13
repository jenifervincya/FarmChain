const TONE_MAP = {
  REGISTERED: 'pending',
  IN_TRANSIT: 'pending',
  OPEN: 'pending',
  PENDING_VALIDATION: 'pending',
  ACCEPTED: 'verified',
  DELIVERED: 'verified',
  RECORDED: 'verified',
  REJECTED_BELOW_FLOOR: 'alert',
  REJECTED: 'alert',
  SENT: 'verified',
  UNRESOLVED: 'alert',
  FAILED: 'alert',
};

export default function StatusBadge({ status }) {
  const tone = TONE_MAP[status] || 'pending';
  return <span className={`stamp ${tone}`}>{status.replace(/_/g, ' ')}</span>;
}
