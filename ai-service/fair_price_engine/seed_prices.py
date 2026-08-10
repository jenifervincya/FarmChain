"""
Placeholder base prices per crop/region, in INR per kg.
This is NOT real market data — it's a reasonable stand-in so the
/ai/fair-price-band endpoint returns genuinely varying, sensible
numbers instead of one hardcoded value. Replace with real historical
data / a trained model per Section 3.2 when available.
"""

BASE_PRICES = {
    ("tomato", "coimbatore"): 21.0,
    ("tomato", "chennai"): 23.5,
    ("onion", "coimbatore"): 18.0,
    ("onion", "chennai"): 19.5,
    ("potato", "coimbatore"): 16.0,
    ("potato", "chennai"): 17.5,
    ("banana", "coimbatore"): 12.0,
    ("banana", "chennai"): 13.0,
}

DEFAULT_BASE_PRICE = 20.0  # fallback for crop/region combos not seeded above


def get_base_price(crop: str, region: str) -> float:
    key = (crop.lower(), region.lower())
    return BASE_PRICES.get(key, DEFAULT_BASE_PRICE)
