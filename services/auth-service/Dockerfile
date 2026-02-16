FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY auth-service/target/quarkus-app/lib/ ./lib/
COPY auth-service/target/quarkus-app/*.jar ./
COPY auth-service/target/quarkus-app/app/ ./app/
COPY auth-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
