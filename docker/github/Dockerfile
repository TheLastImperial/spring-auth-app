ARG DEPLOY_IMAGE=eclipse-temurin
ARG DEPLOY_IMAGE_TAG=21-jre-resolute

FROM ${DEPLOY_IMAGE}:${DEPLOY_IMAGE_TAG} AS deploy

ARG APP_NAME=authapp
ARG VERSION=latest

COPY ./target/${APP_NAME}-${VERSION}.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
