name: Docker Build

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]
  workflow_dispatch:

jobs:
  docker-build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Build backend Docker image
        run: docker build -f VCORE/Dockerfile -t vcore-backend ./VCORE

      - name: Run backend Docker container
        run: |
          docker run -d --name vcore-backend -p 8080:8080 vcore-backend
          sleep 20
          docker logs vcore-backend

      - name: Stop backend Docker container
        if: always()
        run: docker rm -f vcore-backend || true
