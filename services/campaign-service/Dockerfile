####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy parent POM
COPY pom.xml .

# Copy all module POMs
COPY campaign-service-domain/pom.xml ./campaign-service-domain/
COPY campaign-service-view-model/pom.xml ./campaign-service-view-model/
COPY campaign-service-adapter-inbound/pom.xml ./campaign-service-adapter-inbound/
COPY campaign-service-adapter-outbound/pom.xml ./campaign-service-adapter-outbound/
COPY campaign-service-client/pom.xml ./campaign-service-client/
COPY campaign-service/pom.xml ./campaign-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -pl campaign-service -am

# Copy source code for all modules
COPY campaign-service-domain/src ./campaign-service-domain/src
COPY campaign-service-view-model/src ./campaign-service-view-model/src
COPY campaign-service-adapter-inbound/src ./campaign-service-adapter-inbound/src
COPY campaign-service-adapter-outbound/src ./campaign-service-adapter-outbound/src
COPY campaign-service-client/src ./campaign-service-client/src
COPY campaign-service/src ./campaign-service/src

# Build application
RUN mvn clean package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage (main app module)
COPY --from=build /build/campaign-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/campaign-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/campaign-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/campaign-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]