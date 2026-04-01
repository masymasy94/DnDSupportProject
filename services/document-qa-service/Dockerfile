FROM eclipse-temurin:25-jre
WORKDIR /work/
COPY document-qa-service/target/quarkus-app/lib/ ./lib/
COPY document-qa-service/target/quarkus-app/*.jar ./
COPY document-qa-service/target/quarkus-app/app/ ./app/
COPY document-qa-service/target/quarkus-app/quarkus/ ./quarkus/
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/* && mkdir -p /tmp/rag-docs
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
