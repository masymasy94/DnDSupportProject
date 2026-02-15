####
# Multi-stage Dockerfile — compiles inside Docker
# Dependencies: common, notification-service (vm), user-service (client)
####
FROM eclipse-temurin:25-jdk-alpine AS build

ARG MAVEN_VERSION=3.9.9
RUN apk add --no-cache curl && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xzf - -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn

WORKDIR /build
COPY common/ ./common/
RUN --mount=type=cache,target=/root/.m2 cd common && mvn install -DskipTests -q

COPY notification-service/ ./notification-service/
RUN --mount=type=cache,target=/root/.m2 cd notification-service && mvn install -DskipTests -q

COPY user-service/ ./user-service/
RUN --mount=type=cache,target=/root/.m2 cd user-service && mvn install -DskipTests -q

COPY auth-service/ ./auth-service/
RUN --mount=type=cache,target=/root/.m2 cd auth-service && mvn install -DskipTests -q

FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/
COPY --from=build /build/auth-service/auth-service/target/quarkus-app/lib/ ./lib/
COPY --from=build /build/auth-service/auth-service/target/quarkus-app/*.jar ./
COPY --from=build /build/auth-service/auth-service/target/quarkus-app/app/ ./app/
COPY --from=build /build/auth-service/auth-service/target/quarkus-app/quarkus/ ./quarkus/

RUN apk add --no-cache curl

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
