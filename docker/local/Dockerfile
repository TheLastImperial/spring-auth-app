ARG APP_NAME=authapp
ARG VERSION=latest

FROM eclipse-temurin:21-jdk-alpine-3.24 AS builder
WORKDIR /app
COPY ./settings.xml /
COPY . .
RUN ./mvnw --settings /settings.xml clean install package -Dmaven.test.skip

FROM eclipse-temurin:21-jre-alpine-3.24 AS deploy
COPY --from=builder /app/target/${APP_NAME}-${VERSION}.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

