# Use OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/product-catalog-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port used by the service
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
