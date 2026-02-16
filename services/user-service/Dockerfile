FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY user-service/target/quarkus-app/lib/ ./lib/
COPY user-service/target/quarkus-app/*.jar ./
COPY user-service/target/quarkus-app/app/ ./app/
COPY user-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apk add --no-cache curl
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
