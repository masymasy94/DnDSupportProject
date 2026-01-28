####
# Lightweight Dockerfile using pre-built artifacts
# Requires: mvn clean install -DskipTests (run by start.sh)
####
FROM eclipse-temurin:25-jre-alpine

WORKDIR /work/

# Copy pre-built Quarkus application
COPY character-service/target/quarkus-app/lib/ /work/lib/
COPY character-service/target/quarkus-app/*.jar /work/
COPY character-service/target/quarkus-app/app/ /work/app/
COPY character-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
