# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Install Chrome and dependencies for Selenium
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install Chrome
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

# Copy built application from builder stage
COPY --from=builder /app/target /app/target
COPY --from=builder /app/pom.xml /app/pom.xml
COPY --from=builder /app/src /app/src
COPY --from=builder /root/.m2 /root/.m2

# Copy test resources
COPY src/test/resources /app/src/test/resources

# Run tests with headless mode (or skip with -DskipTests if services unavailable)
CMD ["mvn", "clean", "test", "-Dheadless.mode=true", "-Dcucumber.filter.tags=@UI or @API"]
