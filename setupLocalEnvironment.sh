#!/bin/bash

export VAULT_ADDR=https://vault.adeo.no

# Ensure user is authenicated, and run login if not.
gcloud auth print-identity-token &> /dev/null
if [ $? -gt 0 ]; then
    gcloud auth login
fi
kubectl config use-context dev-fss
kubectl config set-context --current --namespace=okonomi

# Get AZURE and DATABASE system variables
envValue=$(kubectl exec -it $(kubectl get pods | grep sokos-nav-oppdrag | cut -f1 -d' ') -c sokos-nav-oppdrag -- env | egrep "^AZURE|^DATABASE|EREG_HOST|TP_HOST|PDL_HOST|PDL_SCOPE")

# Set AZURE as local environment variables
rm -f defaults.properties
echo "$envValue" > defaults.properties
echo "Environment variables stores as defaults.properties"