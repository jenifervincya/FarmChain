"""
Spoilage Decay Score — simplified, rule-based version.
Section 3.2 doesn't specify the exact model; this uses a basic
exponential-style decay driven by time and temperature, common in
food-science heuristics, as a stand-in for a trained model.
"""

from spoilage_model.schemas import SpoilageScoreRequest, SpoilageScoreResponse

# Crop-specific spoilage rate constants (placeholder, not real agri data)
SPOILAGE_RATES = {
    "tomato": 0.020,
    "banana": 0.030,
    "onion": 0.008,
    "potato": 0.006,
}
DEFAULT_RATE = 0.015

# Temperature above this accelerates decay (placeholder threshold)
SAFE_TEMP_C = 8.0


def compute_spoilage_score(request: SpoilageScoreRequest) -> SpoilageScoreResponse:
    rate = SPOILAGE_RATES.get(request.crop.lower(), DEFAULT_RATE)

    temp_penalty = max(0.0, request.transportTemperatureC - SAFE_TEMP_C) * 0.01

    decay_score = min(1.0, rate * request.hoursSinceHarvest + temp_penalty * request.hoursSinceHarvest)

    if decay_score < 0.3:
        risk = "LOW"
    elif decay_score < 0.65:
        risk = "MODERATE"
    else:
        risk = "HIGH"

    return SpoilageScoreResponse(
        batchId=request.batchId,
        decayScore=round(decay_score, 3),
        riskLevel=risk,
        rerouteRecommended=decay_score >= 0.65,
    )
