FROM maven:3-openjdk-17 as build

WORKDIR /usr/src/app

COPY . /usr/src/app
RUN mvn package

FROM build AS run
COPY --from=build /usr/src/app/target/*.jar application.jar

CMD [ "java", "-jar", "application.jar"]
