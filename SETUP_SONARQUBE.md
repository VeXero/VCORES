# SonarCloud setup voor VCORES

## 1. SonarCloud account aanmaken
1. Ga naar https://sonarcloud.io
2. Log in met je GitHub account
3. Geef SonarCloud toegang tot de `VeXero/VCORES` repository

## 2. Project in SonarCloud configureren
1. Klik op **Analyze new project**
2. Kies `VeXero/VCORES`
3. Bevestig:
   - **Project Key**: `VeXero_VCORES`
   - **Organization**: `vexero`
4. Laat SonarCloud de onboarding voltooien

## 3. SONAR_TOKEN aan GitHub Secrets toevoegen
1. Maak in SonarCloud een token aan via **My Account > Security**
2. Ga in GitHub naar:
   - `VeXero/VCORES` → **Settings** → **Secrets and variables** → **Actions**
3. Voeg nieuwe secret toe:
   - **Name**: `SONAR_TOKEN`
   - **Value**: de token uit SonarCloud

## 4. Pipeline valideren
1. Push een commit naar `main`
2. Controleer in **Actions** dat job `sonar` draait bij push events
3. Controleer in SonarCloud dat analysis en coverage zichtbaar zijn
