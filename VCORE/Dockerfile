FROM eclipse-temurin:21 AS builder

WORKDIR /build

COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn ./.mvn

RUN sed -i 's/\r$//' mvnw
RUN ./mvnw dependency:resolve

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/target/VCORE-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
