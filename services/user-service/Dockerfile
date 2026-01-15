####
# Lightweight Dockerfile using pre-built artifacts
# Requires: mvn clean install -DskipTests (run by start.sh)
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /work/

# Copy pre-built Quarkus application
COPY user-service/user-service/target/quarkus-app/lib/ /work/lib/
COPY user-service/user-service/target/quarkus-app/*.jar /work/
COPY user-service/user-service/target/quarkus-app/app/ /work/app/
COPY user-service/user-service/target/quarkus-app/quarkus/ /work/quarkus/

# Install curl for healthchecks
RUN apk add --no-cache curl

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
