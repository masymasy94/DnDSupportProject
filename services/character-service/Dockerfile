####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy parent POM
COPY pom.xml .

# Copy all module POMs
COPY character-service-domain/pom.xml ./character-service-domain/
COPY character-service-view-model/pom.xml ./character-service-view-model/
COPY character-service-adapter-inbound/pom.xml ./character-service-adapter-inbound/
COPY character-service-adapter-outbound/pom.xml ./character-service-adapter-outbound/
COPY character-service-client/pom.xml ./character-service-client/
COPY character-service/pom.xml ./character-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -pl character-service -am

# Copy source code for all modules
COPY character-service-domain/src ./character-service-domain/src
COPY character-service-view-model/src ./character-service-view-model/src
COPY character-service-adapter-inbound/src ./character-service-adapter-inbound/src
COPY character-service-adapter-outbound/src ./character-service-adapter-outbound/src
COPY character-service-client/src ./character-service-client/src
COPY character-service/src ./character-service/src

# Build application
RUN mvn clean package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage (main app module)
COPY --from=build /build/character-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/character-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/character-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/character-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]