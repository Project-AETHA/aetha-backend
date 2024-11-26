FROM bellsoft/liberica-openjdk-alpine:17 AS builder

WORKDIR /home/app
ADD ./ /home/app/aetha

# Ensure mvnw has executable permissions
RUN chmod +x /home/app/aetha/mvnw

# Install dos2unix and Maven
RUN apk update && apk add dos2unix curl bash

# Install Maven manually since it's not included in the base image
RUN curl -o /tmp/apache-maven-3.9.6-bin.tar.gz https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
RUN tar -xzf /tmp/apache-maven-3.9.6-bin.tar.gz -C /opt
RUN ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/local/bin/mvn

# Convert line endings to Unix format
RUN dos2unix /home/app/aetha/mvnw

# Run Maven dependency resolve
RUN mvn -f /home/app/aetha/pom.xml dependency:resolve

# Package the application, skipping tests
RUN cd /home/app/aetha && mvn -Dmaven.test.skip=true clean package


FROM bellsoft/liberica-openjre-alpine:17

WORKDIR /home/app
EXPOSE 8080
SHELL ["/bin/sh", "-c"]

# Run the Spring Boot application
ENTRYPOINT java -jar ./aetha.jar

# Copy the JAR file from the builder stage
COPY --from=builder /home/app/aetha/target/*.jar aetha.jar
