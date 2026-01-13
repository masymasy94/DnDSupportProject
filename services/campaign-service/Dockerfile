####
# Lightweight Dockerfile using pre-built artifacts
# Requires: mvn clean install -DskipTests (run by start.sh)
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy pre-built Quarkus application
COPY campaign-service/target/quarkus-app/lib/ /work/lib/
COPY campaign-service/target/quarkus-app/*.jar /work/
COPY campaign-service/target/quarkus-app/app/ /work/app/
COPY campaign-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
