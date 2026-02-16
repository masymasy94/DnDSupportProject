FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY notification-service/target/quarkus-app/lib/ ./lib/
COPY notification-service/target/quarkus-app/*.jar ./
COPY notification-service/target/quarkus-app/app/ ./app/
COPY notification-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
