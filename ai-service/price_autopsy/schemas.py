from pydantic import BaseModel
from typing import List

class PriceAutopsyRequest(BaseModel):
    """
    NOT defined in Section 4.1. Fields inferred from Section 4.2's
    price-deviation payload summary (batchId, expectedBand, actualPrice,
    nodeChain). Flagged: needs team agreement.
    """
    batchId: str
    expectedMinPrice: float
    expectedMaxPrice: float
    actualPrice: float
    nodeChain: List[str]  # e.g. ["farmer", "transporter", "warehouse", "buyer"]

class PriceAutopsyResponse(BaseModel):
    batchId: str
    responsibleNode: str
    deviationAmount: float
    explanation: str
