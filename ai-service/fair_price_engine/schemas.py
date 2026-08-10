from pydantic import BaseModel
from datetime import datetime


class FactorsResponse(BaseModel):
    weatherImpact: str
    transportCostIndex: float
    crisisSentiment: str


class FairPriceBandResponse(BaseModel):
    crop: str
    region: str
    minPrice: float
    maxPrice: float
    currency: str
    computedAt: datetime
    factors: FactorsResponse
