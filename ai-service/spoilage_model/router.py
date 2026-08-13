from fastapi import APIRouter
from spoilage_model.schemas import SpoilageScoreRequest, SpoilageScoreResponse
from spoilage_model.engine import compute_spoilage_score

router = APIRouter(prefix="/ai", tags=["spoilage-model"])


@router.post("/spoilage-score", response_model=SpoilageScoreResponse)
def get_spoilage_score(request: SpoilageScoreRequest):
    return compute_spoilage_score(request)
