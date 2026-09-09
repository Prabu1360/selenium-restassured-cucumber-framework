# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage - use Ubuntu with both browsers available
FROM ubuntu:22.04

WORKDIR /app

# Install Java 17, Maven, and browsers
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y \
    openjdk-17-jdk \
    maven \
    chromium-browser \
    firefox \
    curl \
    unzip \
    ca-certificates \
    wget \
    && rm -rf /var/lib/apt/lists/* && \
    which mvn && which java

# Copy pom.xml and source code from builder stage
COPY --from=builder /app/pom.xml /app/pom.xml
COPY --from=builder /app/src /app/src

# Copy Maven cache from builder to avoid re-downloading dependencies
COPY --from=builder /root/.m2 /root/.m2

# Run tests with headless mode (browser parameter from Maven or environment)
CMD ["mvn", "clean", "test", "-Dheadless.mode=true"]
