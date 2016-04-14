FROM maven:3.3.3-jdk-8

COPY src /src/src
COPY pom.xml /src/pom.xml

WORKDIR /src

RUN mvn install

EXPOSE 8080

CMD ["mvn", "jetty:run", "-Dorg.eclipse.jetty.annotations.maxWait=120"]




