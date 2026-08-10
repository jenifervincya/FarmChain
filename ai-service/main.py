from fastapi import FastAPI
from fair_price_engine.router import router as fair_price_router

app = FastAPI(title="FairChain AI Service")

app.include_router(fair_price_router)

@app.get("/health")
def health():
    return {"status": "ok"}
