# Build stage
FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Run stage
FROM openjdk:21-slim
WORKDIR /app

# Create a non-root user
RUN groupadd -r springboot && useradd -r -g springboot springboot

# Copy the built artifact from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]
