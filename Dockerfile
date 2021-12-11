FROM openjdk:11-jdk
EXPOSE 8080
COPY out/artifacts/chess_server_main_jar/chess_server.main.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#FROM gradle:7.0-jdk16 AS build
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#RUN gradle build --no-daemon
#
#FROM openjdk:16-jdk
#COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
#
#FROM openjdk:11-jdk AS build-backend
#WORKDIR /app
#COPY . /app
##RUN chmod +x gradlew
#RUN ./gradlew --no-daemon build
#EXPOSE 8080
#CMD ["./gradlew", "--no-daemon", "bootRun"]