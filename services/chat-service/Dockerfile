FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY chat-service/target/quarkus-app/lib/ ./lib/
COPY chat-service/target/quarkus-app/*.jar ./
COPY chat-service/target/quarkus-app/app/ ./app/
COPY chat-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
