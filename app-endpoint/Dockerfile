FROM openjdk:11.0.11-slim-buster

ENV APP_NAME=urlsortener.jar

WORKDIR /app

COPY target/${APP_NAME} app/${APP_NAME}

CMD ["java", "-jar", "/app/urlsortener.jar"]