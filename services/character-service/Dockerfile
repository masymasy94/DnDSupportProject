FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY character-service/target/quarkus-app/lib/ ./lib/
COPY character-service/target/quarkus-app/*.jar ./
COPY character-service/target/quarkus-app/app/ ./app/
COPY character-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
