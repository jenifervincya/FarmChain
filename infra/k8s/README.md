# FairChain K8s configs (demo-level)

Owned by Archana (Frontend/DevOps) per Section 3.3. These are basic
manifests for a local demo cluster (kind/minikube), not production-grade
(no PVCs, no secrets management, no ingress).

## Apply order

```
kubectl apply -f 00-namespace.yaml
kubectl apply -f 10-postgres.yaml
kubectl apply -f 20-zookeeper.yaml
kubectl apply -f 30-kafka.yaml
kubectl apply -f 40-schema-registry.yaml
kubectl apply -f 50-backend.yaml       # needs backend/Dockerfile built first
kubectl apply -f 60-ai-service.yaml    # needs ai-service/Dockerfile built first
kubectl apply -f 70-frontend.yaml      # ready today
```

Or just: `kubectl apply -f .` once all images exist.

## Status

- `frontend`: real, buildable today (`docker build -t fairchain/frontend:latest ./frontend`)
- `backend` / `ai-service`: placeholder image names (`fairchain/backend:latest`,
  `fairchain/ai-service:latest`) — swap these once Jenifer and Anisha add their
  Dockerfiles, then build and load the images into your local cluster
  (`kind load docker-image <image>` or `minikube image load <image>`)
- `postgres` / `kafka` / `zookeeper` / `schema-registry`: standard images, ready as-is

## Accessing the frontend

NodePort 30080. On kind/minikube: `minikube service frontend -n fairchain`
or `kubectl port-forward -n fairchain svc/frontend 8080:80`.
