FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY asset-service/target/quarkus-app/lib/ ./lib/
COPY asset-service/target/quarkus-app/*.jar ./
COPY asset-service/target/quarkus-app/app/ ./app/
COPY asset-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
