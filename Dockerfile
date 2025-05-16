# Build stage
FROM gradle:7.4.2-jdk17-alpine AS build
WORKDIR /app

# Copy gradle files first for caching
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
RUN ./gradlew dependencies

# Copy source and build
COPY src /app/src
RUN ./gradlew build --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
