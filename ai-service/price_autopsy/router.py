from fastapi import APIRouter
from price_autopsy.schemas import PriceAutopsyRequest, PriceAutopsyResponse
from price_autopsy.engine import compute_price_autopsy

router = APIRouter(prefix="/ai", tags=["price-autopsy"])


@router.post("/price-autopsy", response_model=PriceAutopsyResponse)
def get_price_autopsy(request: PriceAutopsyRequest):
    return compute_price_autopsy(request)
