FROM maven:3.8.5-eclipse-temurin-17-alpine AS build
WORKDIR /home/app
COPY pom.xml .
RUN mvn clean
COPY src ./src
RUN ["mvn", "package", "-Dmaven.test.skip=true"]

FROM openjdk:19-slim

RUN apt-get update
RUN apt-get -y install nano
RUN apt-get -y install patch
#COPY . .
COPY --from=build /home/app/target/*.jar /usr/local/lib/build.jar
EXPOSE 8500

ENTRYPOINT ["java","-jar","/usr/local/lib/build.jar"]


# docker build -t project_service_test .  */
# docker run -d -p 8500:8500 --network=host --name project_service project_service_test */
# docker run -d -p 8500:8500 --name project_service project_service_test */