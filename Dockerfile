# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage - use image with Chrome pre-installed
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Install basic tools and Chromium (lighter than Chrome, works for headless)
RUN apt-get update && apt-get install -y \
    chromium-browser \
    curl \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Copy built application from builder stage
COPY --from=builder /app/target /app/target
COPY --from=builder /app/pom.xml /app/pom.xml
COPY --from=builder /app/src /app/src
COPY --from=builder /root/.m2 /root/.m2

# Copy test resources
COPY src/test/resources /app/src/test/resources

# Run tests with headless mode
CMD ["mvn", "clean", "test", "-Dheadless.mode=true"]
