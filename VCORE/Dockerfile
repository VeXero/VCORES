FROM eclipse-temurin:21 AS builder

WORKDIR /build

COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn ./.mvn

RUN ./mvnw dependency:resolve

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
