"""
Fair Price Engine — simplified, rule-based version.

Section 3.2 specifies this should eventually be a regression/LSTM/XGBoost
model using price + weather + transport cost + sentiment data. This is a
placeholder: a transparent formula using the same inputs, so the endpoint
contract and response shape are real and stable now, and the internals
can be swapped for an actual trained model later without any downstream
service (auction-service, Frontend) needing to change.
"""

from datetime import datetime, timezone
from fair_price_engine.seed_prices import get_base_price
from fair_price_engine.schemas import FairPriceBandResponse, FactorsResponse

# Simple weather impact multipliers — placeholder logic
WEATHER_IMPACTS = {
    "clear": 1.00,
    "moderate_rain_delay": 1.08,
    "severe_weather": 1.20,
}

# Simple sentiment multipliers — placeholder logic
SENTIMENT_IMPACTS = {
    "neutral": 1.00,
    "positive": 0.97,
    "crisis": 1.15,
}


def compute_fair_price_band(
    crop: str,
    region: str,
    weather: str = "clear",
    transport_cost_index: float = 1.05,
    sentiment: str = "neutral",
) -> FairPriceBandResponse:
    base_price = get_base_price(crop, region)

    weather_multiplier = WEATHER_IMPACTS.get(weather, 1.0)
    sentiment_multiplier = SENTIMENT_IMPACTS.get(sentiment, 1.0)

    adjusted_price = base_price * weather_multiplier * transport_cost_index * sentiment_multiplier

    # Band width: +/- 12% around the adjusted price, mimicking the
    # spread seen in Section 4.1's sample (18.50-24.00 around ~21.25).
    min_price = round(adjusted_price * 0.94, 2)
    max_price = round(adjusted_price * 1.12, 2)

    return FairPriceBandResponse(
        crop=crop,
        region=region,
        minPrice=min_price,
        maxPrice=max_price,
        currency="INR_per_kg",
        computedAt=datetime.now(timezone.utc),
        factors=FactorsResponse(
            weatherImpact=weather,
            transportCostIndex=transport_cost_index,
            crisisSentiment=sentiment,
        ),
    )
