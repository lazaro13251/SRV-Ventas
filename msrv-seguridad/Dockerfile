FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /source
COPY . .
RUN ./gradlew :msrv-seguridad:build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /source/msrv-seguridad/build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
