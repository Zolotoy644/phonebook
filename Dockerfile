FROM openjdk:17
ADD ./target/phonebook-0.0.1-SNAPSHOT.jar /usr/src/phonebook-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
ENTRYPOINT ["java", "-jar", "phonebook-0.0.1-SNAPSHOT.jar"]