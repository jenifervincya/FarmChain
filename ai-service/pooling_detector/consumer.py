"""
Cooperative Pooling Detector — SIMPLIFIED placeholder.

Section 3.2 specifies this as a real Kafka Streams windowing job.
This is a much lighter stand-in: an in-memory, in-process consumer
loop using kafka-python, grouping batch-registered events by
crop+region within a fixed time window, held in a plain dict (lost on
restart, no persistence, no exactly-once guarantees). Good enough to
demonstrate the pooling-suggested topic firing for a demo, NOT
equivalent to a real Streams topology. Flagged clearly for Anisha to
replace when she's back.
"""

import json
import os
import threading
import time
from collections import defaultdict
from kafka import KafkaConsumer, KafkaProducer

KAFKA_BOOTSTRAP = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092")
BATCH_REGISTERED_TOPIC = "batch-registered"
POOLING_SUGGESTED_TOPIC = "pooling-suggested"

WINDOW_SECONDS = 300       # 5-minute grouping window (placeholder)
MIN_FARMERS_TO_POOL = 3    # placeholder threshold

# In-memory window state: {(crop, region): [(farmerId, batchId, timestamp), ...]}
_window = defaultdict(list)
_lock = threading.Lock()


def _prune_expired(key):
    cutoff = time.time() - WINDOW_SECONDS
    _window[key] = [entry for entry in _window[key] if entry[2] >= cutoff]


def run_pooling_detector():
    consumer = KafkaConsumer(
        BATCH_REGISTERED_TOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP,
        value_deserializer=lambda v: json.loads(v.decode("utf-8")),
        auto_offset_reset="latest",
        group_id="ai-pooling-detector",
    )
    producer = KafkaProducer(
        bootstrap_servers=KAFKA_BOOTSTRAP,
        value_serializer=lambda v: json.dumps(v).encode("utf-8"),
    )

    for message in consumer:
        event = message.value
        crop = event.get("crop")
        region = event.get("region")
        farmer_id = event.get("farmerId")
        batch_id = event.get("batchId")

        if not all([crop, region, farmer_id, batch_id]):
            continue

        key = (crop, region)
        with _lock:
            _prune_expired(key)
            _window[key].append((farmer_id, batch_id, time.time()))

            farmer_ids = list({entry[0] for entry in _window[key]})

            if len(farmer_ids) >= MIN_FARMERS_TO_POOL:
                payload = {
                    "farmerIds": farmer_ids,
                    "crop": crop,
                    "region": region,
                    "window": f"{WINDOW_SECONDS}s",
                }
                producer.send(POOLING_SUGGESTED_TOPIC, value=payload)
                producer.flush()
                _window[key] = []  # reset after suggesting a pool


def start_background():
    thread = threading.Thread(target=run_pooling_detector, daemon=True)
    thread.start()
