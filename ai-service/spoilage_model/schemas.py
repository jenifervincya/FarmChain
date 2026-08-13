from pydantic import BaseModel

class SpoilageScoreRequest(BaseModel):
    """
    NOT defined in Section 4.1 (no sample JSON given for this endpoint).
    Fields inferred from Section 1.4 step 6 ("AI monitors freshness decay
    using transport/weather data") and Section 4.2's transport-event
    payload (which includes temperature). Flagged: needs team agreement.
    """
    batchId: str
    crop: str
    hoursSinceHarvest: float
    transportTemperatureC: float

class SpoilageScoreResponse(BaseModel):
    batchId: str
    decayScore: float  # 0.0 (fresh) to 1.0 (spoiled)
    riskLevel: str      # LOW, MODERATE, HIGH
    rerouteRecommended: bool
