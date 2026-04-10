FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /source
COPY . .
RUN ./gradlew :msrv-ventas:build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /source/msrv-ventas/build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
