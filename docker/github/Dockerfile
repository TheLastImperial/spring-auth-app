ARG APP_NAME=authapp
ARG VERSION=latest

FROM eclipse-temurin:21-jre-focal AS deploy

COPY ./target/${APP_NAME}-${VERSION}.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
