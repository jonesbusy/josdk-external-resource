#!/bin/bash

for i in $(seq -w 1 200); do
  NS="test-namespace-$i"
  echo "Creating namespace: $NS"
  kubectl create namespace "$NS"

  USER=$(openssl rand -hex 8)
  PASS=$(openssl rand -hex 12)

  echo "Creating secret in $NS"
  kubectl create secret generic random-secret \
    --from-literal=username="$USER" \
    --from-literal=password="$PASS" \
    -n "$NS"
done
