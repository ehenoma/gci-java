#!/usr/bin/env bash
cd ../
./gradlew sonarqube \
  -Dsonar.projectKey=merlinweber_gci-java \
  -Dsonar.organization=merlinweber-github \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=0e68123bdca2388781295174c6bce034ffeddddc