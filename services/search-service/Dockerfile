####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy parent POM
COPY pom.xml .

# Copy all module POMs
COPY search-service-domain/pom.xml ./search-service-domain/
COPY search-service-view-model/pom.xml ./search-service-view-model/
COPY search-service-adapter-inbound/pom.xml ./search-service-adapter-inbound/
COPY search-service-adapter-outbound/pom.xml ./search-service-adapter-outbound/
COPY search-service-client/pom.xml ./search-service-client/
COPY search-service/pom.xml ./search-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -pl search-service -am

# Copy source code for all modules
COPY search-service-domain/src ./search-service-domain/src
COPY search-service-view-model/src ./search-service-view-model/src
COPY search-service-adapter-inbound/src ./search-service-adapter-inbound/src
COPY search-service-adapter-outbound/src ./search-service-adapter-outbound/src
COPY search-service-client/src ./search-service-client/src
COPY search-service/src ./search-service/src

# Build application
RUN mvn clean package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage (main app module)
COPY --from=build /build/search-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/search-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/search-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/search-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]