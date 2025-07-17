# Use Eclipse Temurin JDK 17 as the base image for the build stage
FROM eclipse-temurin:17 AS builder

# Set the working directory inside the container
WORKDIR workspace

# Define build argument for the JAR file location (Maven default)
ARG JAR_FILE=target/*.jar

# Copy the JAR file from the Maven target directory to the container
COPY ${JAR_FILE} catalog-service.jar

# Extract the JAR file layers using Spring Boot's layertools
# This separates dependencies, loader, and application code for better caching
RUN java -Djarmode=layertools -jar catalog-service.jar extract

# Start the runtime stage with a fresh Eclipse Temurin JRE 17 image
FROM eclipse-temurin:17

# Create a non-root user named 'overbugger' for security
RUN useradd overbugger

# Switch to the non-root user
USER overbugger

# Set the working directory for the runtime stage
WORKDIR workspace

# Copy the extracted layers from the builder stage in dependency order
# Dependencies layer (external libraries) - changes least frequently
COPY --from=builder workspace/dependencies/ ./

# Spring Boot loader layer - framework code
COPY --from=builder workspace/spring-boot-loader/ ./

# Snapshot dependencies layer - SNAPSHOT versions of dependencies
COPY --from=builder workspace/snapshot-dependencies/ ./

# Application layer (your code) - changes most frequently
COPY --from=builder workspace/application/ ./

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]