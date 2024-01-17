FROM maven:3-openjdk-17 as build

WORKDIR /usr/src/app

COPY . /usr/src/app
RUN mvn --no-transfer-progress -DskipTests package

FROM build AS run
COPY --from=build /usr/src/app/target/*.jar application.jar

CMD [ "java", "-Dspring.profiles.active=docker", "-jar", "application.jar"]
