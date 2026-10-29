ARG BUILDER_IMAGE_TAG=21-jdk-alpine-3.24
ARG DEPLOY_IMAGE_TAG=21-jre-alpine-3.24

FROM eclipse-temurin:${BUILDER_IMAGE_TAG} AS builder
WORKDIR /app
COPY ./settings.xml /
COPY . .
RUN ./mvnw --settings /settings.xml clean install package -Dmaven.test.skip

FROM eclipse-temurin:${DEPLOY_IMAGE_TAG} AS deploy

ARG APP_NAME=authapp
ARG VERSION=latest

COPY --from=builder /app/target/${APP_NAME}-${VERSION}.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
