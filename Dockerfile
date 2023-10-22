FROM maven:3.8.4-openjdk-17 AS MAVEN_TOOL_CHAIN
# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies for offline use
RUN mvn dependency:go-offline -B

WORKDIR /src/
COPY . /src/
RUN ls -l /src/
RUN mvn clean install -DskipTests
CMD java  -jar target/order-svc-0.0.1-SNAPSHOT.jar
