#stage 1
#Start with a base image containing Java runtime
FROM openjdk:11-slim as build

# Add Maintainer Info
LABEL maintainer="HLimam <heithem.limame@gmail.com>"

# The application's jar file
#ARG JAR_FILE

# Add the application's jar to the container
COPY target/*.jar app.jar

#unpackage jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

#stage 2
#Same Java runtime
FROM openjdk:11-slim

#Add volume pointing to /tmp
VOLUME /tmp

RUN apt-get update
RUN apt-get -y install nano
RUN apt-get -y install patch
RUN apt-get -y install dos2unix
RUN mkdir "tmpFilesConvert"
RUN mkdir "tmpDelta"

#Copy unpackaged application to new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

#execute the application
ENTRYPOINT ["java","-cp","app:app/lib/*","ProjectServiceApplication"]

# docker build -t project_service_test .  */
# docker run -d -p 8500:8500 --network=host --name project_service project_service_test */
# docker run -d -p 8500:8500 --name project_service project_service_test */
