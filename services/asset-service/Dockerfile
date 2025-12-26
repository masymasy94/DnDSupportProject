####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy pom.xml and download dependencies (for better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN mvn package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage
COPY --from=build /build/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/target/quarkus-app/*.jar /work/
COPY --from=build /build/target/quarkus-app/app/ /work/app/
COPY --from=build /build/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]