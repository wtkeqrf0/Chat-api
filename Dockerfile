FROM openjdk
COPY build/libs/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]