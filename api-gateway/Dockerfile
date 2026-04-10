FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /source
COPY . .
RUN ./gradlew :api-gateway:build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /source/api-gateway/build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
