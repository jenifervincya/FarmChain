from pydantic import BaseModel

class NegotiateRequest(BaseModel):
    """NOT defined in Section 4.1. Inferred from Section 1.3's description
    of an 'AI Negotiation Agent that acts on the farmer's behalf'.
    Flagged: needs team agreement."""
    batchId: str
    currentOffer: float
    floorPrice: float
    ceilingPrice: float

class NegotiateResponse(BaseModel):
    batchId: str
    decision: str  # ACCEPT, COUNTER, REJECT
    counterOffer: float | None
    rationale: str
