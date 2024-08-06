# Use the official OpenJDK image as the base image
FROM openjdk:17

# Expose port 8080 to the outside world
EXPOSE 8080

# Define the JAR file name as an argument
ARG JAR_FILE=target/aetha_backend-0.0.1-SNAPSHOT.jar

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Specify the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]