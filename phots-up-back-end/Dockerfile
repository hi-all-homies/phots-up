FROM alpine:latest

RUN apk add openjdk11

ADD ./build/libs/phots-up-back.jar /home/

WORKDIR /home

CMD java $JAVA_OPTS -jar phots-up-back.jar --server.port=$PORT
