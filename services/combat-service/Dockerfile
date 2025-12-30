####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy parent POM
COPY pom.xml .

# Copy all module POMs
COPY combat-service-domain/pom.xml ./combat-service-domain/
COPY combat-service-view-model/pom.xml ./combat-service-view-model/
COPY combat-service-adapter-inbound/pom.xml ./combat-service-adapter-inbound/
COPY combat-service-adapter-outbound/pom.xml ./combat-service-adapter-outbound/
COPY combat-service-client/pom.xml ./combat-service-client/
COPY combat-service/pom.xml ./combat-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -pl combat-service -am

# Copy source code for all modules
COPY combat-service-domain/src ./combat-service-domain/src
COPY combat-service-view-model/src ./combat-service-view-model/src
COPY combat-service-adapter-inbound/src ./combat-service-adapter-inbound/src
COPY combat-service-adapter-outbound/src ./combat-service-adapter-outbound/src
COPY combat-service-client/src ./combat-service-client/src
COPY combat-service/src ./combat-service/src

# Build application
RUN mvn clean package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage (main app module)
COPY --from=build /build/combat-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/combat-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/combat-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/combat-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]