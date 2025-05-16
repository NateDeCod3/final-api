# Use official OpenJDK base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy build files
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle gradle

# Copy source code
COPY src src

# Build the application
RUN ./gradlew build --no-daemon

# Expose the port your app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "build/libs/your-backend-app.jar"]