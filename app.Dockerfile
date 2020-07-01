# --- Build ---
FROM maven:latest AS builder

# Download all dependencies first to so that when there is changes in source
# code, docker image cache can still be used
WORKDIR /usr/src/app
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

COPY . /usr/src/app
# offline somehow does not work and maven downloads libraries even though
# pom.xml has not been changed.
# RUN mvn package --offline -Dmaven.test.skip=true
RUN mvn package -Dmaven.test.skip=true

# --- Run ---
FROM openjdk:latest
COPY --from=builder /usr/src/app/target/JavalinExp-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/JavalinExp.jar
EXPOSE 8080
CMD ["java", "-jar", "/opt/JavalinExp.jar"]
