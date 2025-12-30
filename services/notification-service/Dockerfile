####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy parent POM
COPY pom.xml .

# Copy all module POMs
COPY notification-service-domain/pom.xml ./notification-service-domain/
COPY notification-service-view-model/pom.xml ./notification-service-view-model/
COPY notification-service-adapter-inbound/pom.xml ./notification-service-adapter-inbound/
COPY notification-service-adapter-outbound/pom.xml ./notification-service-adapter-outbound/
COPY notification-service-client/pom.xml ./notification-service-client/
COPY notification-service/pom.xml ./notification-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -pl notification-service -am

# Copy source code for all modules
COPY notification-service-domain/src ./notification-service-domain/src
COPY notification-service-view-model/src ./notification-service-view-model/src
COPY notification-service-adapter-inbound/src ./notification-service-adapter-inbound/src
COPY notification-service-adapter-outbound/src ./notification-service-adapter-outbound/src
COPY notification-service-client/src ./notification-service-client/src
COPY notification-service/src ./notification-service/src

# Build application
RUN mvn clean package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage (main app module)
COPY --from=build /build/notification-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/notification-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/notification-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/notification-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]