FROM gradle:7.4.2-jdk17 as Builder

ARG BUILD_HOME=/home/build/
WORKDIR $BUILD_HOME
COPY ./ $BUILD_HOME
RUN gradle :registry:build --no-daemon

FROM --platform=linux/amd64 openjdk:17-jdk-alpine as production

WORKDIR /home/app/
COPY --from=builder $BUILD_HOME/registry/build/libs/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]