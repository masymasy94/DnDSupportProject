####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy and build common module first
COPY common/pom.xml ./common/
COPY common/src ./common/src
RUN cd common && mvn clean install -DskipTests

# Copy parent POM
COPY auth-service/pom.xml .

# Copy all module POMs
COPY auth-service/auth-service-domain/pom.xml ./auth-service-domain/
COPY auth-service/auth-service-view-model/pom.xml ./auth-service-view-model/
COPY auth-service/auth-service-adapter-inbound/pom.xml ./auth-service-adapter-inbound/
COPY auth-service/auth-service-adapter-outbound/pom.xml ./auth-service-adapter-outbound/
COPY auth-service/auth-service-client/pom.xml ./auth-service-client/
COPY auth-service/auth-service/pom.xml ./auth-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -pl auth-service -am

# Copy source code for all modules
COPY auth-service/auth-service-domain/src ./auth-service-domain/src
COPY auth-service/auth-service-view-model/src ./auth-service-view-model/src
COPY auth-service/auth-service-adapter-inbound/src ./auth-service-adapter-inbound/src
COPY auth-service/auth-service-adapter-outbound/src ./auth-service-adapter-outbound/src
COPY auth-service/auth-service-client/src ./auth-service-client/src
COPY auth-service/auth-service/src ./auth-service/src

# Build application
RUN mvn clean package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage (main app module)
COPY --from=build /build/auth-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/auth-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/auth-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/auth-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]