FROM arm64v8/openjdk:8-jdk-alpine

VOLUME /tmp
COPY PincheSystem-back-0.0.1-SNAPSHOT.jar /pinche.jar
EXPOSE 82

RUN apk add --no-cache busybox-extras

ENTRYPOINT ["java","-jar","/pinche.jar"]
