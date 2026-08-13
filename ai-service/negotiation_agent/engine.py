"""
Negotiation Agent — simplified rule-based version, not a trained/learned
negotiation strategy. Accepts offers close to ceiling, counters offers
in the middle of the band, rejects offers below floor.
"""

from negotiation_agent.schemas import NegotiateRequest, NegotiateResponse


def negotiate(request: NegotiateRequest) -> NegotiateResponse:
    if request.currentOffer < request.floorPrice:
        return NegotiateResponse(
            batchId=request.batchId,
            decision="REJECT",
            counterOffer=None,
            rationale=f"Offer {request.currentOffer} is below the fair floor of {request.floorPrice}.",
        )

    if request.currentOffer >= request.ceilingPrice * 0.95:
        return NegotiateResponse(
            batchId=request.batchId,
            decision="ACCEPT",
            counterOffer=None,
            rationale="Offer is at or near the top of the fair band — accepting.",
        )

    # Counter roughly halfway between the offer and the ceiling
    counter = round((request.currentOffer + request.ceilingPrice) / 2, 2)
    return NegotiateResponse(
        batchId=request.batchId,
        decision="COUNTER",
        counterOffer=counter,
        rationale=f"Offer is within the band but below ceiling; countering at {counter}.",
    )
