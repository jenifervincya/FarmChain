from fastapi import APIRouter
from negotiation_agent.schemas import NegotiateRequest, NegotiateResponse
from negotiation_agent.engine import negotiate

router = APIRouter(prefix="/ai", tags=["negotiation-agent"])


@router.post("/negotiate", response_model=NegotiateResponse)
def negotiate_offer(request: NegotiateRequest):
    return negotiate(request)
