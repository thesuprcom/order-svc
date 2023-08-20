FROM maven:3.8.4-openjdk-17 AS MAVEN_TOOL_CHAIN
# copy the project files
COPY ./pom.xml ./pom.xml

WORKDIR /src/
COPY . /src/
RUN mvn clean install
CMD java  -jar target/order-svc-0.0.1-SNAPSHOT.jar
