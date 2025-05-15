# Build stage
FROM maven:3.8.2-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/SocialMedia.jar SocialMedia.jar
EXPOSE 8080
CMD ["java", "-jar", "SocialMedia.jar"]
