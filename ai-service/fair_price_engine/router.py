from fastapi import APIRouter, Query
from fair_price_engine.engine import compute_fair_price_band
from fair_price_engine.schemas import FairPriceBandResponse

router = APIRouter(prefix="/ai", tags=["fair-price-engine"])


@router.get("/fair-price-band", response_model=FairPriceBandResponse)
def get_fair_price_band(
    crop: str = Query(..., description="Crop name, e.g. tomato"),
    region: str = Query(..., description="Region name, e.g. coimbatore"),
):
    return compute_fair_price_band(crop=crop, region=region)
