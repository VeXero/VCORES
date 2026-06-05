# SonarCloud Setup Guide

Follow these steps to connect this repository to SonarCloud and enable CI analysis.

## 1. Create a SonarCloud account

1. Go to [https://sonarcloud.io](https://sonarcloud.io).
2. Sign in with your GitHub account.
3. Create or select your SonarCloud organization.

## 2. Import this repository

1. In SonarCloud, click **+** > **Analyze new project**.
2. Select `VeXero/VCORES`.
3. Keep the project key as `VeXero_VCORES` (or match `sonar-project.properties`).

## 3. Generate a Sonar token

1. Open SonarCloud account settings.
2. Go to **Security**.
3. Generate a token and copy it immediately.

## 4. Add GitHub secret

1. Open GitHub repository: **Settings** > **Secrets and variables** > **Actions**.
2. Click **New repository secret**.
3. Add:
   - **Name:** `SONAR_TOKEN`
   - **Value:** your SonarCloud token

## 5. Verify workflow execution

1. Push to `main` or `develop`.
2. Open **Actions** in GitHub and check the `Java CI/CD with Maven` workflow.
3. Confirm `sonar-analysis` completes successfully.

## 6. Confirm analysis in SonarCloud

1. Open the project in SonarCloud.
2. Verify:
   - Code analysis appears for the latest commit.
   - Coverage is imported from JaCoCo reports.
   - Quality gate status is shown.
