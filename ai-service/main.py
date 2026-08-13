from fastapi import FastAPI
from fair_price_engine.router import router as fair_price_router
from spoilage_model.router import router as spoilage_router
from price_autopsy.router import router as price_autopsy_router
from negotiation_agent.router import router as negotiate_router
from pooling_detector.consumer import start_background as start_pooling_detector

app = FastAPI(title="FairChain AI Service")

app.include_router(fair_price_router)
app.include_router(spoilage_router)
app.include_router(price_autopsy_router)
app.include_router(negotiate_router)


@app.on_event("startup")
def on_startup():
    # Starts the simplified pooling detector consumer loop in the
    # background. If Kafka isn't reachable, this thread will just
    # keep retrying/failing silently in the background — doesn't
    # block the REST API from serving requests.
    start_pooling_detector()


@app.get("/health")
def health():
    return {"status": "ok"}
