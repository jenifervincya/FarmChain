"""
Price Autopsy — simplified placeholder.
A real implementation would trace ledger events (Section 2.1: "AI
services can read from [the ledger] via Backend API for Price Autopsy
computation") to find where costs/delays were introduced. This version
just flags the deviation and picks the last node in the chain as a
naive guess — NOT a real root-cause analysis. Flagged clearly.
"""

from price_autopsy.schemas import PriceAutopsyRequest, PriceAutopsyResponse


def compute_price_autopsy(request: PriceAutopsyRequest) -> PriceAutopsyResponse:
    deviation = request.actualPrice - request.expectedMinPrice

    if request.actualPrice >= request.expectedMinPrice:
        return PriceAutopsyResponse(
            batchId=request.batchId,
            responsibleNode="none",
            deviationAmount=round(deviation, 2),
            explanation="Price is within or above the fair band; no deviation detected.",
        )

    # Naive placeholder: blame the last node before the buyer (commonly
    # the middleman/transporter in the described problem, per Section 1.2).
    # A real model would need actual ledger event data to reason about this.
    responsible = request.nodeChain[-2] if len(request.nodeChain) >= 2 else "unknown"

    return PriceAutopsyResponse(
        batchId=request.batchId,
        responsibleNode=responsible,
        deviationAmount=round(deviation, 2),
        explanation=f"Price fell {abs(deviation):.2f} below the fair floor. "
                    f"Placeholder analysis flags '{responsible}' as the likely node — "
                    f"NOT based on real ledger event tracing yet.",
    )
