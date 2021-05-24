# syntax=docker/dockerfile:1

FROM gradle:6.9-jdk11 AS build
COPY --chown=gradle:gradle ./src /home/gradle/src/src
COPY --chown=gradle:gradle ./gradle /home/gradle/src/gradle
COPY --chown=gradle:gradle ./gradlew /home/gradle/src/gradlew
COPY --chown=gradle:gradle ./build.gradle /home/gradle/src/build.gradle
WORKDIR /home/gradle/src
RUN gradle bootwar --no-daemon


# Build image
FROM openjdk:11.0.6-jre
EXPOSE 80

ENV dbAddress=localhost
ENV dbPort=3306
ENV dbSchema=battlemap
ENV dbUser=mapWatcher
ENV dbPassword=password

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.war MapExplorer.war

ENTRYPOINT ["java", "-jar", "MapExplorer.war"]