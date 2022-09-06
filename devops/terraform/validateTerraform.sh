#!/usr/bin/env bash
# exit when any command fails
set -e
export ENVIRONMENT=dev
export DEVOPS_PATH=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "${DEVOPS_PATH}"
terraform init -input=false -backend=false -upgrade
terraform validate
