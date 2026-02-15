####
# Multi-stage Dockerfile — compiles inside Docker
# Dependencies: common (annotation processors)
####
FROM eclipse-temurin:25-jdk-alpine AS build

ARG MAVEN_VERSION=3.9.9
RUN apk add --no-cache curl && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xzf - -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn

WORKDIR /build
COPY common/ ./common/
RUN --mount=type=cache,target=/root/.m2 cd common && mvn install -DskipTests -q

COPY campaign-service/ ./campaign-service/
RUN --mount=type=cache,target=/root/.m2 cd campaign-service && mvn install -DskipTests -q

FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY --from=build /build/campaign-service/campaign-service/target/quarkus-app/lib/ ./lib/
COPY --from=build /build/campaign-service/campaign-service/target/quarkus-app/*.jar ./
COPY --from=build /build/campaign-service/campaign-service/target/quarkus-app/app/ ./app/
COPY --from=build /build/campaign-service/campaign-service/target/quarkus-app/quarkus/ ./quarkus/

RUN apk add --no-cache curl

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
