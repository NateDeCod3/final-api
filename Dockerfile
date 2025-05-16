# Build stage - using official Maven image with JDK 17
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app

# Copy just the POM first for better layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src /app/src
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
