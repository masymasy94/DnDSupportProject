FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY compendium-service/target/quarkus-app/lib/ ./lib/
COPY compendium-service/target/quarkus-app/*.jar ./
COPY compendium-service/target/quarkus-app/app/ ./app/
COPY compendium-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
