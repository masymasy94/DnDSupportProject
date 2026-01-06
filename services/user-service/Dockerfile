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
COPY user-service/pom.xml .

# Copy all module POMs
COPY user-service/user-service-domain/pom.xml ./user-service-domain/
COPY user-service/user-service-view-model/pom.xml ./user-service-view-model/
COPY user-service/user-service-adapter-inbound/pom.xml ./user-service-adapter-inbound/
COPY user-service/user-service-adapter-outbound/pom.xml ./user-service-adapter-outbound/
COPY user-service/user-service-client/pom.xml ./user-service-client/
COPY user-service/user-service/pom.xml ./user-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -pl user-service -am

# Copy source code for all modules
COPY user-service/user-service-domain/src ./user-service-domain/src
COPY user-service/user-service-view-model/src ./user-service-view-model/src
COPY user-service/user-service-adapter-inbound/src ./user-service-adapter-inbound/src
COPY user-service/user-service-adapter-outbound/src ./user-service-adapter-outbound/src
COPY user-service/user-service-client/src ./user-service-client/src
COPY user-service/user-service/src ./user-service/src

# Build application
RUN mvn clean package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage (main app module)
COPY --from=build /build/user-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/user-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/user-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/user-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]