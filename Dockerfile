# Use an official Java runtime as the base image
FROM eclipse-temurin:24-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the application files into the container
COPY . .

# Build the application (for Maven-based projects)
RUN mvn clean package

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/SocialMedia-0.0.1-SNAPSHOT.jar"]
