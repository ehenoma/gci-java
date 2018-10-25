#!/usr/bin/env bash
# Simple scripts that runs the sonarqube analysis.

readonly PROJECT_KEY="merlinweber_gci-java"
readonly PROJECT_ORG="merlinweber-github"
readonly LOGIN_TOKEN="0e68123bdca2388781295174c6bce034ffeddddc"

# Move to the gradlew file home.
cd ../

./gradlew sonarqube \
  -Dsonar.projectKey=${PROJECT_KEY} -Dsonar.organization=${PROJECT_ORG} \
  -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=${LOGIN_TOKEN}