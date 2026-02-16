FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY search-service/target/quarkus-app/lib/ ./lib/
COPY search-service/target/quarkus-app/*.jar ./
COPY search-service/target/quarkus-app/app/ ./app/
COPY search-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
