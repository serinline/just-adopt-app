# Use the official maven/Java 8 image to create a build artifact.
FROM maven:3.8-jdk-11 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
FROM adoptopenjdk/openjdk11:alpine-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/just-adopt-*.jar /just-adopt.jar

# Run the web service on container startup.
CMD ["java", "-Djava.security.egd=file:/dev/./urandom -Djdk.tls.client.protocols=TLSv1.2", "-jar", "/just-adopt.jar"]