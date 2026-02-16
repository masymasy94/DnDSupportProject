FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY campaign-service/target/quarkus-app/lib/ ./lib/
COPY campaign-service/target/quarkus-app/*.jar ./
COPY campaign-service/target/quarkus-app/app/ ./app/
COPY campaign-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
