# Step 1: Use an official, lightweight Java 17 runtime as the base image
FROM eclipse-temurin:17-jdk-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the compiled JAR file from the target directory to the container
COPY target/*.jar app.jar

# Step 4: Document the internal port
EXPOSE 8083

# Step 5: Define the startup command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]