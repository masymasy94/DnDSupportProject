####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy parent POM
COPY pom.xml .

# Copy all module POMs
COPY chat-service-domain/pom.xml ./chat-service-domain/
COPY chat-service-view-model/pom.xml ./chat-service-view-model/
COPY chat-service-adapter-inbound/pom.xml ./chat-service-adapter-inbound/
COPY chat-service-adapter-outbound/pom.xml ./chat-service-adapter-outbound/
COPY chat-service-client/pom.xml ./chat-service-client/
COPY chat-service/pom.xml ./chat-service/

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -pl chat-service -am

# Copy source code for all modules
COPY chat-service-domain/src ./chat-service-domain/src
COPY chat-service-view-model/src ./chat-service-view-model/src
COPY chat-service-adapter-inbound/src ./chat-service-adapter-inbound/src
COPY chat-service-adapter-outbound/src ./chat-service-adapter-outbound/src
COPY chat-service-client/src ./chat-service-client/src
COPY chat-service/src ./chat-service/src

# Build application
RUN mvn clean package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy the built artifact from build stage (main app module)
COPY --from=build /build/chat-service/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/chat-service/target/quarkus-app/*.jar /work/
COPY --from=build /build/chat-service/target/quarkus-app/app/ /work/app/
COPY --from=build /build/chat-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]