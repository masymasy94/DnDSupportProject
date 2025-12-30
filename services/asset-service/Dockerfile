####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy parent pom and all module poms
COPY pom.xml .
COPY asset-service-domain/pom.xml asset-service-domain/
COPY asset-service-view-model/pom.xml asset-service-view-model/
COPY asset-service-adapter-inbound/pom.xml asset-service-adapter-inbound/
COPY asset-service-adapter-outbound/pom.xml asset-service-adapter-outbound/
COPY asset-service-client/pom.xml asset-service-client/
COPY asset-service/pom.xml asset-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline

# Copy source code for all modules
COPY asset-service-domain/src asset-service-domain/src
COPY asset-service-view-model/src asset-service-view-model/src
COPY asset-service-adapter-inbound/src asset-service-adapter-inbound/src
COPY asset-service-adapter-outbound/src asset-service-adapter-outbound/src
COPY asset-service-client/src asset-service-client/src
COPY asset-service/src asset-service/src

# Build application
RUN mvn package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage
COPY --from=build /build/asset-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/asset-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/asset-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/asset-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8085

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]