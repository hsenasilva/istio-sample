FROM openjdk:8-jdk-alpine
ENV APP_FILE demo1-0.1.0-SNAPSHOT.jar
ENV APP_HOME /usr/app
EXPOSE 8080
COPY target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $APP_FILE"]
